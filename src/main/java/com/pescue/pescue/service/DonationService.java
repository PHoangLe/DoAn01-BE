package com.pescue.pescue.service;

import com.pescue.pescue.dto.DonationDTO;
import com.pescue.pescue.exception.DonationCompletedException;
import com.pescue.pescue.exception.DonationDeclinedException;
import com.pescue.pescue.exception.DonationNotFoundException;
import com.pescue.pescue.exception.UpdateFundException;
import com.pescue.pescue.model.*;
import com.pescue.pescue.repository.DonationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DonationService {
    private final DonationRepository donationRepository;
    private final UserService userService;
    private final FundService fundService;
    private final FundTransactionService fundTransactionService;
    public Donation createDonation(DonationDTO dto){
        Fund fund = fundService.getFundByFundID(dto.getFundID());
        User user = userService.findUserByID(dto.getUserID());

        Donation donation = new Donation(user, fund, dto.getNumsOfPackage());
        return donationRepository.insert(donation);
    }
    public List<Donation> getAllDonation(){
        return donationRepository.findAll();
    }
    public Donation getDonationByID(String donationID){
        return donationRepository.findById(donationID).orElseThrow(DonationNotFoundException::new);
    }
    public void confirmDonation(String donationID) throws UpdateFundException, DonationCompletedException {
        Donation donation = getDonationByID(donationID);

        if (donation.getDonationStatus() == DonationStatus.COMPLETED)
            throw new DonationCompletedException();

        Fund fund = donation.getFund();

        BigDecimal value = fund.getValuePerDonationPackage().multiply(BigDecimal.valueOf(donation.getNumsOfPackage()));

        fundTransactionService.createTransaction(TransactionType.USER_TO_FUND, fund.getFundID(), donation.getUser(), value);
        donation.setDonationStatus(DonationStatus.COMPLETED);
        donationRepository.save(donation);
    }
    public void rejectDonation(String donationID) throws DonationDeclinedException {
        Donation donation = getDonationByID(donationID);

        if (donation.getDonationStatus() == DonationStatus.REJECTED)
            throw new DonationDeclinedException();

        donation.setDonationStatus(DonationStatus.REJECTED);
        donationRepository.save(donation);
    }
}
