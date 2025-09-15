package com.eventbooking.Event.Booking.Services;

import com.eventbooking.Event.Booking.DTO.UserDTO;
import com.eventbooking.Event.Booking.Entities.UserEntity;
import com.eventbooking.Event.Booking.Exceptions.ResourceNotFoundException;
import com.eventbooking.Event.Booking.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    public UserDTO registerUser(UserDTO userData) {
        if (userRepository.findByEmail(userData.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        UserEntity userEntity = modelMapper.map(userData, UserEntity.class);
        return modelMapper.map(userRepository.save(userEntity), UserDTO.class);
    }


    public UserDTO loginUser(UserDTO userData){
        UserEntity user = userRepository.findByEmail(userData.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("No User Found with email: " + userData.getEmail()));

        if (!user.getPassword().equals(userData.getPassword())){
            throw new RuntimeException("Invalid password");
        }
        return modelMapper.map(user, UserDTO.class);
    }

    public UserDTO getUserById(Long id){
        UserEntity userEntity=userRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("User Not Found With Id: "+id));
        return modelMapper.map(userEntity,UserDTO.class);
    }

    public List<UserDTO> getAllUsers() {
        List<UserEntity> userEntities = userRepository.findAll();
        return userEntities.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .toList();
    }

}
