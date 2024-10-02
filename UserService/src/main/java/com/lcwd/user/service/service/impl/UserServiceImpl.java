package com.lcwd.user.service.service.impl;

import com.lcwd.user.service.entities.Hotel;
import com.lcwd.user.service.entities.Rating;
import com.lcwd.user.service.entities.User;
import com.lcwd.user.service.exception.ResourceNotFoundException;
import com.lcwd.user.service.external.services.HotelService;
import com.lcwd.user.service.repository.UserRepository;
import com.lcwd.user.service.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    Logger logger =  LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HotelService hotelService;

    @Override
    public User saveUser(User user) {
        //generate unique userid
        String randomUserId = UUID.randomUUID().toString();
        user.setUserId(randomUserId);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    /*@Override
    public User getUserId(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with given id is not found on server: " + userId));

        // Fetch ratings for the user using ParameterizedTypeReference
        ResponseEntity<List<Rating>> responseEntity = restTemplate.exchange("http://RATING-MS/ratings/users/" + user.getUserId(),HttpMethod.GET,null,new ParameterizedTypeReference<List<Rating>>() {}
        );

        List<Rating> ratingOfUser = responseEntity.getBody();

        // Handle potential null ratings
        if (ratingOfUser == null) {
            ratingOfUser = new ArrayList<>();
        }

        List<Rating> ratingLists = ratingOfUser.stream().map(rating -> {
            ResponseEntity<Hotel> forEntity = restTemplate.getForEntity("http://HOTEL-MS/hotels/" + rating.getHotelId(), Hotel.class);

            // Check if the response is successful
            if (forEntity.getStatusCode().is2xxSuccessful()) {
                Hotel hotel = forEntity.getBody();
                logger.info("Successfully retrieved hotel with ID: {}", rating.getHotelId());
                rating.setHotel(hotel);
            } else {
                logger.warn("Failed to retrieve hotel with ID: {}. Status code: {}", rating.getHotelId(), forEntity.getStatusCode());
                rating.setHotel(null); // Handle this case appropriately
            }
            return rating;
        }).collect(Collectors.toList());

        logger.info("User ratings: {}", ratingLists);
        user.setRatings(ratingLists);

        return user;
    }*/

    @Override
    public User getUserId(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with given id is not found on server: " + userId));

        // Fetch ratings for the user using ParameterizedTypeReference
        ResponseEntity<List<Rating>> responseEntity = restTemplate.exchange("http://RATING-MS/ratings/users/" + user.getUserId(),HttpMethod.GET,null,new ParameterizedTypeReference<List<Rating>>() {}
        );

        List<Rating> ratingOfUser = responseEntity.getBody();

        // Handle potential null ratings
        if (ratingOfUser == null) {
            ratingOfUser = new ArrayList<>();
        }

        List<Rating> ratingLists = ratingOfUser.stream().map(rating -> {
        Hotel hotel = hotelService.getHotel(rating.getHotelId());
        rating.setHotel(hotel);
            return rating;
        }).collect(Collectors.toList());

        logger.info("User ratings: {}", ratingLists);
        user.setRatings(ratingLists);

        return user;
    }

}
