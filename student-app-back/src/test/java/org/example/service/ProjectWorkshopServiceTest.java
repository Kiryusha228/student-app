package org.example.service;

import org.example.mapper.ProjectWorkshopMapper;
import org.example.model.dto.database.CreateProjectWorkshopDto;
import org.example.model.entity.ProjectWorkshopEntity;
import org.example.repository.ProjectWorkshopRepository;
import org.example.service.impl.ProjectWorkshopServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectWorkshopServiceTest {
    @Mock
    private ProjectWorkshopRepository projectWorkshopRepository;

    @Mock
    private ProjectWorkshopMapper projectWorkshopMapper;

    @InjectMocks
    private ProjectWorkshopServiceImpl projectWorkshopService;

    @Test
    void createProjectWorkshop() {
        //Arrange
        CreateProjectWorkshopDto createProjectWorkshopDto = new CreateProjectWorkshopDto();
        createProjectWorkshopDto.setName("Test Workshop");
        createProjectWorkshopDto.setYear(2025);

        ProjectWorkshopEntity projectWorkshopEntity = new ProjectWorkshopEntity();
        projectWorkshopEntity.setName("Test Workshop");
        projectWorkshopEntity.setYear(2025);

        when(projectWorkshopMapper.toProjectWorkshopEntity(createProjectWorkshopDto))
                .thenReturn(projectWorkshopEntity);

        //Act
        projectWorkshopService.createProjectWorkshop(createProjectWorkshopDto);

        //Assert
        verify(projectWorkshopMapper).toProjectWorkshopEntity(createProjectWorkshopDto);
        verify(projectWorkshopRepository).save(projectWorkshopEntity);
    }

    @Test
    void addStudent() {
    }

    @Test
    void getProjectWorkshops() {
    }

    @Test
    void getProjectWorkshopById() {
    }

    @Test
    void getLastProjectWorkshopId() {
    }

    @Test
    void getTeams() {
    }
}