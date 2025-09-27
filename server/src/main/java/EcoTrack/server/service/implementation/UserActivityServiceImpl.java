package EcoTrack.server.service.implementation;

import EcoTrack.server.DTO.ActivityDTO;
import EcoTrack.server.DTO.UserActivityDTO;
import EcoTrack.server.entity.ActivityType;
import EcoTrack.server.entity.User;
import EcoTrack.server.entity.UserActivity;
import EcoTrack.server.exception.NotFoundException;
import EcoTrack.server.repository.ActivityTypeRepository;
import EcoTrack.server.repository.UserActivityRepository;
import EcoTrack.server.repository.UserRepository;
import EcoTrack.server.service.UserActivityService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserActivityServiceImpl implements UserActivityService {
    private final UserActivityRepository userActivityRepository;
    private final ActivityTypeRepository activityTypeRepository;
    private final UserRepository userRepository;
    private final ScoreServiceImpl scoreServiceImpl;

    public UserActivityServiceImpl(UserActivityRepository userActivityRepository, ActivityTypeRepository activityTypeRepository, UserRepository userRepository, ScoreServiceImpl scoreServiceImpl) {
        this.userActivityRepository = userActivityRepository;
        this.activityTypeRepository = activityTypeRepository;
        this.userRepository = userRepository;
        this.scoreServiceImpl = scoreServiceImpl;
    }

    @Override
    public UserActivityDTO findDTOById(Long id) {
        Optional<UserActivity> userActivity = userActivityRepository.findById(id);
        return userActivity.map(UserActivityDTO::new).orElse(null);
    }

    @Override
    public List<UserActivityDTO> findAllDTO() {
        return userActivityRepository.findAll().stream().map(UserActivityDTO::new).toList();
    }

    @Override
    public void deleteDTOById(Long id) {
        userActivityRepository.deleteById(id);
    }

    @Override
    public UserActivityDTO createDTO(UserActivityDTO userActivityDTO) {
        UserActivity userActivity = new UserActivity(userActivityDTO);

        ActivityType activityType = activityTypeRepository.findById(userActivityDTO.getActivityTypeId())
                .orElseThrow(() -> new NotFoundException("ActivityType not found with : " + userActivityDTO.getActivityTypeId()));
        userActivity.setActivityType(activityType);

        User user = userRepository.findById(userActivityDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found with : " + userActivityDTO.getUserId()));
        userActivity.setUser(user);

        return new UserActivityDTO(userActivityRepository.save(userActivity));

    }

    @Override
    public UserActivityDTO updateDTO(UserActivityDTO userActivityDTO) {
        UserActivity userActivity = userActivityRepository.findById(userActivityDTO.getId())
                .orElseThrow(() -> new NotFoundException("UserActivity not found with : " + userActivityDTO.getId()));

        userActivity.setQuantity(userActivityDTO.getQuantity());
        userActivity.setNbrPersonnes(userActivityDTO.getNbrPersonnes());
        userActivity.setDate(userActivityDTO.getDate());


        ActivityType activityType = activityTypeRepository.findById(userActivityDTO.getActivityTypeId())
                .orElseThrow(() -> new NotFoundException("ActivityType not found with : " + userActivityDTO.getActivityTypeId()));
        userActivity.setActivityType(activityType);

        User user = userRepository.findById(userActivityDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found with : " + userActivityDTO.getUserId()));
        userActivity.setUser(user);

        return new UserActivityDTO(userActivityRepository.save(userActivity));
    }
    @Override
    public UserActivityDTO createActivity(ActivityDTO dto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ActivityType type = activityTypeRepository.findByName(dto.getActivityTypeName())
                .orElseThrow(() -> new RuntimeException("ActivityType not found"));

        UserActivity activity = new UserActivity();
        activity.setQuantity(dto.getQuantity());
        activity.setNbrPersonnes(dto.getNbrPersonnes());
        activity.setDate(dto.getDate());
        activity.setUser(user);
        activity.setSharingType(dto.getSharingType());
        activity.setActivityType(type);
        UserActivity savedActivity = userActivityRepository.save(activity);
        scoreServiceImpl.calculateAndSaveScore(savedActivity);
        return new UserActivityDTO(savedActivity);

    }

    @Override
    public List<UserActivity> getUserActivitiesByUserId(Long id){
        List<UserActivity> userActivities = userActivityRepository.findUserActivityByUserId(id);
        return userActivities;
    }
}
