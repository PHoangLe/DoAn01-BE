package com.pescue.pescue.service;

import com.pescue.pescue.dto.RescuePostDTO;
import com.pescue.pescue.exception.RescuePostNotFoundException;
import com.pescue.pescue.exception.RescuePostStatusUpdateException;
import com.pescue.pescue.model.FundingRequest;
import com.pescue.pescue.model.RescuePost;
import com.pescue.pescue.model.Shelter;
import com.pescue.pescue.model.User;
import com.pescue.pescue.model.constant.RescuePostStatus;
import com.pescue.pescue.repository.RescuePostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RescuePostService {
    private final RescuePostRepository rescuePostRepository;
    private final UserService userService;
    private final ShelterService shelterService;
    private final EmailService emailService;
    private void sendNotifyEmail(RescuePost request, RescuePostStatus status) {
        String emailBody = "";
        switch (status){
            case ABORTED -> {
                emailBody = "Chúng tôi rất tiếc khi việc cứu trợ bé bạn đã đăng không thể thực hiện đuọc\n" +
                        "Xin lỗi vè việc không vui này, chúng tôi vẫn mong bạn vẫn đồng hành cùng với Pescue để có thể cứu được nhiều bé thú cưng vẫn đang gặp hoàn cảnh khó khăn,\n" +
                        "Ban quản trị";
            }
            case COMPLETED -> {
                emailBody = "Chúng tôi xin thông báo đến bằng rằng bé thú cưng bạn đã đăng đã được trại cứu trợ " + request.getRescuer().getShelterName() + " giải cứu thành công\n" +
                        "Cảm ơn bạn đã đồng hành với Pescue,\n" +
                        "Ban quản trị";
            }
            case PROCESSING -> {
                emailBody = "Chúng tôi xin thông báo đến bằng bé thú cưng bạn đăng đã được trại cứu trợ " + request.getRescuer().getShelterName() + " tiếp nhận việc giải cứu\n" +
                        "Cảm ơn bạn đã đồng hành với Pescue,\n" +
                        "Ban quản trị";
            }
        }
        emailService.sendMail(request.getPoster().getUserEmail(),
                emailBody,
                "Trạng thái bài đăng cứu trợ");
    }
    public RescuePost createPost(RescuePostDTO dto){
        User poster = userService.getUserByID(dto.getUserID());

        RescuePost newPost = new RescuePost(
                dto.getImages(), poster,
                dto.getAnimalDescription(), dto.getLocationDescription(),
                dto.getStreet(), dto.getWard(), dto.getDistrict(), dto.getCity());

        return rescuePostRepository.insert(newPost);
    }
    public RescuePost updatePostWithDTO(String rescuePostID, RescuePostDTO dto) {
        RescuePost post = getByPostID(rescuePostID);

        post.setDTO(dto);

        return updatePost(post);
    }
    public RescuePost updatePost(RescuePost post){
        return rescuePostRepository.save(post);
    }
    public void deletePost(String rescuePostID){
        rescuePostRepository.deleteById(rescuePostID);
    }
    public RescuePost getByPostID(String rescuePostID){
        return rescuePostRepository.findById(rescuePostID).orElseThrow(RescuePostNotFoundException::new);
    }
    public List<RescuePost> getAllPost(){
        return rescuePostRepository.findAll();
    }
    public List<RescuePost> getAllByStatus(RescuePostStatus status){
        return rescuePostRepository.findAllByStatus(status);
    }
    public List<RescuePost> getAllByUserID(String userID){
        return rescuePostRepository.findAllByPoster(userID);
    }
    public List<RescuePost> getAllByShelterID(String shelterID){
        return rescuePostRepository.findAllByRescuer(shelterID);
    }
    public List<List<RescuePost>> getAllForShelterRescuePage(String shelterID){
        List<List<RescuePost>> waitingPostAndProcessingPost = new ArrayList<>();

        List<RescuePost> allPost = getAllPost();

        List<RescuePost> waitingPost = allPost.stream()
                .filter((post) -> post.getStatus() == RescuePostStatus.WAITING)
                .toList();

        List<RescuePost> processingPost = allPost.stream()
                .filter((post) -> post.getStatus() == RescuePostStatus.PROCESSING)
                .filter((post) -> post.getRescuer().getShelterID().equals(shelterID))
                .toList();

        waitingPostAndProcessingPost.add(waitingPost);
        waitingPostAndProcessingPost.add(processingPost);

        return waitingPostAndProcessingPost;
    }
    public RescuePost acceptRescuePost(String shelterID, String rescuePostID){
        Shelter shelter = shelterService.getShelterByShelterID(shelterID);
        RescuePost rescuePost = getByPostID(rescuePostID);

        if (rescuePost.getStatus() != RescuePostStatus.WAITING)
            throw new RescuePostStatusUpdateException();

        rescuePost.setRescuer(shelter);
        rescuePost.setStatus(RescuePostStatus.PROCESSING);
        sendNotifyEmail(rescuePost, RescuePostStatus.PROCESSING);

        return updatePost(rescuePost);
    }
    public RescuePost completeRescuePost(String rescuePostID){
        RescuePost rescuePost = getByPostID(rescuePostID);

        if (rescuePost.getStatus() != RescuePostStatus.PROCESSING)
            throw new RescuePostStatusUpdateException();

        rescuePost.setStatus(RescuePostStatus.COMPLETED);
        sendNotifyEmail(rescuePost, RescuePostStatus.COMPLETED);

        return updatePost(rescuePost);
    }
    public RescuePost abortRescuePost(String rescuePostID){
        RescuePost rescuePost = getByPostID(rescuePostID);

        if (rescuePost.getStatus() != RescuePostStatus.PROCESSING)
            throw new RescuePostStatusUpdateException();

        rescuePost.setStatus(RescuePostStatus.ABORTED);
        sendNotifyEmail(rescuePost, RescuePostStatus.ABORTED);

        return updatePost(rescuePost);

    }
}
