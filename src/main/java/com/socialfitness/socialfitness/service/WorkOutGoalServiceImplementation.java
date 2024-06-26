package com.socialfitness.socialfitness.service;

import com.socialfitness.socialfitness.models.Post;
import com.socialfitness.socialfitness.models.User;
import com.socialfitness.socialfitness.models.WorkOutGoal;
import com.socialfitness.socialfitness.repository.WorkOutGoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WorkOutGoalServiceImplementation implements WorkOutGoalService{

    @Autowired
    WorkOutGoalRepository postRepository;
    @Autowired
    UserService userService;


    @Override
    public WorkOutGoal createNewWorkOutGoalPost(WorkOutGoal post, Integer userId) throws Exception {

        User user = userService.findUserById(userId);
        WorkOutGoal newPost = new WorkOutGoal();
        newPost.setCaption(post.getCaption());
        newPost.setDistanceRun(post.getDistanceRun());
        newPost.setPushupsCompleted(post.getPushupsCompleted());
        newPost.setWeightLifted(post.getWeightLifted());
        newPost.setCreatedAt(LocalDateTime.now());
        newPost.setUser(user);

        return postRepository.save(newPost);
    }

    @Override
    public String deleteWorkOutGoalPost(Integer postId, Integer userId) throws Exception {
        WorkOutGoal post = findWorkOutGoalPostById(postId);
        User user = userService.findUserById(userId);

        if(post.getUser().getId()!=user.getId()){
            throw new Exception("You can not delete another users post.");
        }

        postRepository.delete(post);

        return "post deleted successfully";
    }

    @Override
    public List<WorkOutGoal> findWorkOutGoalPostByUserId(Integer userId) throws Exception {

        return postRepository.findWorkOutGoalPostByUserId(userId);
    }

    @Override
    public WorkOutGoal findWorkOutGoalPostById(Integer postId) throws Exception {
        Optional<WorkOutGoal> opt = postRepository.findById(postId);
        if(opt.isEmpty()){
            throw new Exception("post not found with id " + postId);
        }
        return opt.get();
    }

    @Override
    public List<WorkOutGoal> findAllWorkOutGoalPost() {
        return postRepository.findAll();
    }

    @Override
    public WorkOutGoal likeWorkOutGoalPost(Integer postId, Integer userId) throws Exception {
        WorkOutGoal post = findWorkOutGoalPostById(postId);
        User user = userService.findUserById(userId);

        if(post.getLiked().contains(user)){
            post.getLiked().remove(user);
        }else{
            post.getLiked().add(user);
        }


        return postRepository.save(post);
    }

    public WorkOutGoal updateGoalPost(Integer postId, WorkOutGoal post, Integer userId) throws Exception {

        Optional<WorkOutGoal> post1 = postRepository.findById(postId);
        WorkOutGoal oldPost = post1.get();
        User user = userService.findUserById(userId);

        if (post1.isEmpty()) {
            throw new Exception("Post not exit with id" + postId);
        }

        if(oldPost.getUser().getId()!=user.getId()){
            throw new Exception("You can not update another users post.");
        }

        if (post.getCaption() != null) {
            oldPost.setCaption(post.getCaption());
        }

        if (post.getDistanceRun() != 0.0) {
            oldPost.setDistanceRun(post.getDistanceRun());
        }

        if (post.getPushupsCompleted() != 0) {
            oldPost.setPushupsCompleted(post.getPushupsCompleted());
        }

        if (post.getWeightLifted() != 0.0) {
            oldPost.setWeightLifted(post.getWeightLifted());
        }

        WorkOutGoal updatedPost = postRepository.save(oldPost);

        return updatedPost;
    }
}
