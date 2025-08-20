package EcoTrack.server.service.implementation;

import EcoTrack.server.DTO.EmissionFactorDTO;
import EcoTrack.server.entity.ActivityType;
import EcoTrack.server.entity.Country;
import EcoTrack.server.entity.EmissionFactor;
import EcoTrack.server.exception.NotFoundException;
import EcoTrack.server.repository.ActivityTypeRepository;
import EcoTrack.server.repository.CountryRepository;
import EcoTrack.server.repository.EmissionFactorRepository;
import EcoTrack.server.service.EmissionFactorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmissionFactorServiceImpl implements EmissionFactorService {
    private final EmissionFactorRepository emissionFactorRepository;
    private final CountryRepository countryRepository;
    private final ActivityTypeRepository activityTypeRepository;

    public EmissionFactorServiceImpl(EmissionFactorRepository emissionFactorRepository, CountryRepository countryRepository, ActivityTypeRepository activityTypeRepository) {
        this.emissionFactorRepository = emissionFactorRepository;
        this.countryRepository = countryRepository;
        this.activityTypeRepository = activityTypeRepository;
    }

    @Override
    public EmissionFactorDTO findDTOById(Long id) {
        Optional<EmissionFactor> emissionFactor = emissionFactorRepository.findById(id);
        return emissionFactor.map(EmissionFactorDTO::new)
                .orElseThrow(() -> new NotFoundException("EmissionFactor not found with : " + id));
    }

    @Override
    public List<EmissionFactorDTO> findAllDTO() {
        return emissionFactorRepository.findAll().stream()
                .map(EmissionFactorDTO::new).toList();
    }

    @Override
    public void deleteDTOById(Long id) {
        emissionFactorRepository.deleteById(id);
    }

    @Override
    public EmissionFactorDTO createDTO(EmissionFactorDTO emissionFactorDTO) {
        EmissionFactor emissionFactor = new EmissionFactor(emissionFactorDTO);
        Country country = countryRepository.findById(emissionFactorDTO.getCountryId())
                .orElseThrow(() -> new NotFoundException("Country not found with : " + emissionFactorDTO.getCountryId()));

        ActivityType activityType = activityTypeRepository.findById(emissionFactorDTO.getActivityTypeId())
                .orElseThrow(() -> new NotFoundException("ActivityType not found with : " + emissionFactorDTO.getActivityTypeId()));

        emissionFactor.setActivityType(activityType);
        emissionFactor.setCountry(country);

        return new EmissionFactorDTO(emissionFactorRepository.save(emissionFactor));
    }

    @Override
    public EmissionFactorDTO updateDTO(EmissionFactorDTO emissionFactorDTO) {
        EmissionFactor emissionFactor = emissionFactorRepository.findById(emissionFactorDTO.getId())
                .orElseThrow(() -> new NotFoundException("Emission Factor not found with : " + emissionFactorDTO.getId()));
        emissionFactor.setSource(emissionFactorDTO.getSource());
        emissionFactor.setFactor(emissionFactorDTO.getFactor());

        Country country = countryRepository.findById(emissionFactorDTO.getCountryId())
                .orElseThrow(() -> new NotFoundException("Country not found with : " + emissionFactorDTO.getCountryId()));
        emissionFactor.setCountry(country);

        ActivityType activityType = activityTypeRepository.findById(emissionFactorDTO.getActivityTypeId())
                .orElseThrow(() -> new NotFoundException("ActivityType not found with : " + emissionFactorDTO.getActivityTypeId()));
        emissionFactor.setActivityType(activityType);

        return new EmissionFactorDTO(emissionFactorRepository.save(emissionFactor));
    }
}
