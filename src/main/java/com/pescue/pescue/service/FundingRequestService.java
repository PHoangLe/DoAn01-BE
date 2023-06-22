package com.pescue.pescue.service;


import com.pescue.pescue.dto.FundingRequestDTO;
import com.pescue.pescue.exception.*;
import com.pescue.pescue.model.*;
import com.pescue.pescue.model.constant.FundingRequestStatus;
import com.pescue.pescue.model.constant.TransactionType;
import com.pescue.pescue.repository.FundingRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FundingRequestService {
    private final FundingRequestRepository fundingRequestRepository;
    private final FundService fundService;
    private final UserService userService;
    private final ShelterService shelterService;
    private final FundTransactionService fundTransactionService;
    private final EmailService emailService;
    private void sendNotifyEmail(FundingRequest request, boolean isApprove) {
        String emailBody;
        if (isApprove){
            emailBody = "Đăng ký nhận quỹ cứu trợ của bạn đã được chấp thuận. Bạn hãy kiểm tra tài khoản của mình. Nếu có thắc mắc xin hãy liên hệ lại với chúng tôi qua email này!\n" +
                    "Cảm ơn bạn đã đồng hành với Pescue\n" +
                    "Ban quản trị";
        }
        else {
            emailBody = """
                    Chúng tôi rất tiếc khi phải thông báo rằng yêu cầu nhận quỹ cứu trợ của bạn đã bị từ chối.
                    Để biết thêm thông tin chi tiết xin vui lòng liên lạc lại với chúng tôi
                    Hoặc bạn có thể gửi lại yêu cầu với lý do đầy đủ hơn.""";
        }

        emailService.sendMail(request.getUser().getUserEmail(),
                emailBody,
                "Kết quả đăng ký nhận quỹ cứu trợ");
    }
    public FundingRequest createFundingRequest(FundingRequestDTO dto){
        Fund fund = fundService.getFundByFundID(dto.getFundID());
        Shelter shelter = shelterService.getShelterByShelterID(dto.getShelterID());
        User user = userService.getUserByID(shelter.getUserID());

        FundingRequest fundingRequest = new FundingRequest(fund, user, shelter, dto.getReason(), dto.getValue());
        return fundingRequestRepository.insert(fundingRequest);
    }
    public FundingRequest getFundingRequestByID(String requestID){
        return fundingRequestRepository.findById(requestID).orElseThrow(FundingRequestNotFoundException::new);
    }
    public List<FundingRequest> getAllFundingRequest(){
        return fundingRequestRepository.findAll();
    }
    public List<FundingRequest> getAllFundingRequestByUserID(String userID) {
        return fundingRequestRepository.findAllByUser(userID);
    }
    public void updateFundingRequest(FundingRequest request){
        fundingRequestRepository.save(request);
    }
    public void confirmFundingRequest(String requestID) throws UpdateFundException, FundingStatusUpdateException, FundEmptyBalanceException {
        FundingRequest request = getFundingRequestByID(requestID);

        if (request.getRequestStatus() != FundingRequestStatus.PENDING)
            throw new FundingStatusUpdateException();

        Fund fund = request.getFund();
        BigDecimal value = request.getValue();

        if (fund.getFundBalance().compareTo(value) < 0)
            throw new FundEmptyBalanceException();


        updateShelterTotalFundingReceive(request.getShelter(), value);
        createTransactionFromFundToShelter(fund, request, value);
        updateFundingRequest(request);

        sendNotifyEmail(request, true);
    }

    private void updateShelterTotalFundingReceive(Shelter shelter, BigDecimal value) {
        BigDecimal totalFundingReceived = shelter.getTotalFundReceived() != null ? shelter.getTotalFundReceived() : BigDecimal.ZERO;

        shelter.setTotalFundReceived(totalFundingReceived.add(value));
        shelterService.updateShelter(shelter);
    }

    public void createTransactionFromFundToShelter(Fund fund, FundingRequest request, BigDecimal value){
        fundTransactionService.createTransaction(TransactionType.FUND_TO_USER, fund.getFundID(), request.getUser(), value);
        request.setRequestStatus(FundingRequestStatus.COMPLETED);
    }
    public void rejectFundingRequest(String requestID) throws FundingStatusUpdateException {
        FundingRequest request = getFundingRequestByID(requestID);

        if (request.getRequestStatus() != FundingRequestStatus.PENDING)
            throw new FundingStatusUpdateException();

        request.setRequestStatus(FundingRequestStatus.REJECTED);
        fundingRequestRepository.save(request);

        sendNotifyEmail(request, false);
    }
}
