package EcoTrack.server.service.implementation;

import EcoTrack.server.DTO.HistoricAdviceDTO;
import EcoTrack.server.entity.Advice;
import EcoTrack.server.entity.HistoricAdvice;
import EcoTrack.server.entity.User;
import EcoTrack.server.exception.NotFoundException;
import EcoTrack.server.repository.AdviceRepository;
import EcoTrack.server.repository.HistoricAdviceRepository;
import EcoTrack.server.repository.UserRepository;
import EcoTrack.server.service.HistoricAdviceService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HistoricAdviceServiceImpl implements HistoricAdviceService {
    private final HistoricAdviceRepository historicAdviceRepository;
    private final AdviceRepository adviceRepository;
    private final UserRepository userRepository;

    public HistoricAdviceServiceImpl(HistoricAdviceRepository historicAdviceRepository, AdviceRepository adviceRepository, UserRepository userRepository) {
        this.historicAdviceRepository = historicAdviceRepository;
        this.adviceRepository = adviceRepository;
        this.userRepository = userRepository;
    }

    @Override
    public HistoricAdviceDTO findDTOById(Long id) {
        Optional<HistoricAdvice> historicAdvice = historicAdviceRepository.findById(id);
        return historicAdvice.map(HistoricAdviceDTO::new)
                .orElseThrow(() -> new NotFoundException("HistoricAdvice not found with : " + id));
    }

    @Override
    public List<HistoricAdviceDTO> findAllDTO() {
        return historicAdviceRepository.findAll()
                .stream().map(HistoricAdviceDTO::new).toList();
    }

    @Override
    public void deleteDTOById(Long id) {
        historicAdviceRepository.deleteById(id);
    }

    @Override
    public HistoricAdviceDTO updateDTO(HistoricAdviceDTO historicAdviceDTO) {
        HistoricAdvice historicAdvice = historicAdviceRepository.findById(historicAdviceDTO.getId())
                .orElseThrow(() -> new NotFoundException("HistoricAdvice not found with : " + historicAdviceDTO.getId()));

        User user = userRepository.findById(historicAdviceDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found with : " + historicAdviceDTO.getUserId()));
        historicAdvice.setUser(user);

        Advice advice = adviceRepository.findById(historicAdviceDTO.getAdviceId())
                .orElseThrow(() -> new NotFoundException("Advice not found with : " + historicAdviceDTO.getAdviceId()));
        historicAdvice.setAdvice(advice);


        return new HistoricAdviceDTO(historicAdviceRepository.save(historicAdvice));
    }

    @Override
    public HistoricAdviceDTO createDTO(HistoricAdviceDTO historicAdviceDTO) {
        HistoricAdvice historicAdvice = new HistoricAdvice(historicAdviceDTO);

        Advice advice = adviceRepository.findById(historicAdviceDTO.getAdviceId())
                .orElseThrow(() -> new NotFoundException("Advice not found with : " + historicAdviceDTO.getAdviceId()));
        historicAdvice.setAdvice(advice);
        User user = userRepository.findById(historicAdviceDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found with : " + historicAdviceDTO.getUserId()));
        historicAdvice.setUser(user);

        return new HistoricAdviceDTO(historicAdviceRepository.save(historicAdvice));

    }
}
