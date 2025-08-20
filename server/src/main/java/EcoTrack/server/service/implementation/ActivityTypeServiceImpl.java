package EcoTrack.server.service.implementation;

import EcoTrack.server.DTO.ActivityTypeDTO;
import EcoTrack.server.entity.ActivityType;
import EcoTrack.server.entity.Category;
import EcoTrack.server.exception.NotFoundException;
import EcoTrack.server.repository.ActivityTypeRepository;
import EcoTrack.server.repository.CategoryRepository;
import EcoTrack.server.service.ActivityTypeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActivityTypeServiceImpl implements ActivityTypeService {

    private final ActivityTypeRepository activityTypeRepository;
    private final CategoryRepository categoryRepository;
    public ActivityTypeServiceImpl(ActivityTypeRepository activityTypeRepository, CategoryRepository categoryRepository) {
        this.activityTypeRepository = activityTypeRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<ActivityTypeDTO> findAllDTO() {
        return activityTypeRepository.findAll()
                .stream().map(ActivityTypeDTO::new).toList();
    }

    @Override
    public ActivityTypeDTO findDTOById(Long id) {
        Optional<ActivityType> activityTypeOptional = activityTypeRepository.findById(id);
        return activityTypeOptional.map(ActivityTypeDTO::new)
                .orElseThrow(() -> new NotFoundException("ActivtyType not found with : " + id));
    }

    @Override
    public ActivityTypeDTO updateDTO(ActivityTypeDTO activityTypeDTO) {
        ActivityType activityType = activityTypeRepository.findById(activityTypeDTO.getId())
                .orElseThrow(() -> new NotFoundException("ActivtyType not found with : " + activityTypeDTO.getId()));

        activityType.setName(activityTypeDTO.getName());
        activityType.setUnit(activityTypeDTO.getUnit());

            Category category = categoryRepository.findById(activityTypeDTO.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Category not found with : " + activityTypeDTO.getCategoryId()));
            activityType.setCategory(category);



        return new ActivityTypeDTO(activityTypeRepository.save(activityType));
    }

    @Override
    public ActivityTypeDTO createDTO(ActivityTypeDTO activityTypeDTO) {
        ActivityType activityType = new ActivityType(activityTypeDTO);

        Category category = categoryRepository.findById(activityTypeDTO.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found with : " + activityTypeDTO.getCategoryId()));
        activityType.setCategory(category);

        return new ActivityTypeDTO(activityTypeRepository.save(activityType));

    }

    @Override
    public void deleteDTOById(Long id) {
        activityTypeRepository.deleteById(id);
    }

}
