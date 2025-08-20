package EcoTrack.server.service.implementation;

import EcoTrack.server.DTO.UserActivityDTO;
import EcoTrack.server.entity.ActivityType;
import EcoTrack.server.entity.Household;
import EcoTrack.server.entity.User;
import EcoTrack.server.entity.UserActivity;
import EcoTrack.server.exception.NotFoundException;
import EcoTrack.server.repository.ActivityTypeRepository;
import EcoTrack.server.repository.HouseholdRepository;
import EcoTrack.server.repository.UserActivityRepository;
import EcoTrack.server.repository.UserRepository;
import EcoTrack.server.service.UserActivityService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserActivityServiceImpl implements UserActivityService {
    private final UserActivityRepository userActivityRepository;
    private final ActivityTypeRepository activityTypeRepository;
    private final HouseholdRepository householdRepository;
    private final UserRepository userRepository;

    public UserActivityServiceImpl(UserActivityRepository userActivityRepository, ActivityTypeRepository activityTypeRepository, HouseholdRepository householdRepository, UserRepository userRepository) {
        this.userActivityRepository = userActivityRepository;
        this.activityTypeRepository = activityTypeRepository;
        this.householdRepository = householdRepository;
        this.userRepository = userRepository;
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

        Household household = householdRepository.findById(userActivityDTO.getHouseholdId())
                .orElseThrow(() -> new NotFoundException("Household not found with : " + userActivityDTO.getHouseholdId()));
        userActivity.setHousehold(household);

        User user = userRepository.findById(userActivityDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found with : " + userActivityDTO.getUserId()));
        userActivity.setUser(user);

        return new UserActivityDTO(userActivityRepository.save(userActivity));

    }

    @Override
    public UserActivityDTO updateDTO(UserActivityDTO userActivityDTO) {
        UserActivity userActivity = userActivityRepository.findById(userActivityDTO.getId())
                .orElseThrow(() -> new NotFoundException("UserActivity not found with : " + userActivityDTO.getId()));

        ActivityType activityType = activityTypeRepository.findById(userActivityDTO.getActivityTypeId())
                .orElseThrow(() -> new NotFoundException("ActivityType not found with : " + userActivityDTO.getActivityTypeId()));
        userActivity.setActivityType(activityType);

        Household household = householdRepository.findById(userActivityDTO.getHouseholdId())
                .orElseThrow(() -> new NotFoundException("Household not found with : " + userActivityDTO.getHouseholdId()));
        userActivity.setHousehold(household);

        User user = userRepository.findById(userActivityDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found with : " + userActivityDTO.getUserId()));
        userActivity.setUser(user);

        return new UserActivityDTO(userActivityRepository.save(userActivity));
    }
}
