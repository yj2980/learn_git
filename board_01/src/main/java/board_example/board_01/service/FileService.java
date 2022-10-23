package board_example.board_01.service;

import board_example.board_01.domain.entity.File;
import board_example.board_01.domain.repository.FileRepository;
import board_example.board_01.dto.FileDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class FileService {
    private FileRepository fileRepository;

    public FileService(FileRepository fileRepository)
    {
        this.fileRepository = fileRepository;
    }

    @Transactional
    public long saveFile(FileDto fileDto)
    {
        return fileRepository.save(fileDto.toEntity()).getId();
    }

    @Transactional
    public FileDto getFile(Long id)
    {
        File file = fileRepository.findById(id).get();

        FileDto fileDto = FileDto.builder()
                .id(file.getId())
                .origFilename(file.getOrigFilename())
                .filename(file.getFilename())
                .filePath(file.getFilePath())
                .build();
        return fileDto;
    }

    @Transactional
    public void deletePost(Long id) {
        fileRepository.deleteById(id);
    }
}
