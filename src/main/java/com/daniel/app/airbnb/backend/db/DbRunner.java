package com.daniel.app.airbnb.backend.db;

import com.daniel.app.airbnb.backend.environment.AdminEnv;
import com.daniel.app.airbnb.backend.model.*;
import com.daniel.app.airbnb.backend.model.enums.Provider;
import com.daniel.app.airbnb.backend.model.enums.Role;
import com.daniel.app.airbnb.backend.repository.AirBnbRepository;
import com.daniel.app.airbnb.backend.repository.UserRepository;
import com.daniel.app.airbnb.backend.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Year;
import java.util.*;


@Component
@RequiredArgsConstructor
@Slf4j
public class DbRunner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminEnv adminEnv;
    private final AirBnbRepository airBnbRepository;
    private final ImageService imageService;

    @Override
    public void run(String... args) throws Exception {
        log.info("[SAVING ADMIN]");
        User user = new User();
        Optional<User> userFromDb = userRepository.findUserByEmail(adminEnv.getAppEmail());
        if(userFromDb.isEmpty()){
            user.setSuperhost(true);
            user.setRoles(Set.of(Role.ROLE_ADMIN));
            user.setName(adminEnv.getAppUsername());
            user.setEmail(adminEnv.getAppEmail());
            user.setPassword(passwordEncoder.encode(adminEnv.getAppPassword()));
            user.setProvider(Provider.LOCAL);
            user.setJoinedYear(Year.now().getValue());
            user.setVerified(true);
            userRepository.save(user);
            log.info("[SAVING AIRBNBS]");
            airBnbRepository.saveAll(listings(user));
        }




    }


    private List<AirBnbListing> listings(User user) {
        return List.of(
                new AirBnbListing(null, "Charming Studio in Downtown", user,
                        new Location(null, "New York", "NY", "USA"),
                        new ArrayList<>(),
                        imageService.randomHouseImage(),
                        new Host(null, "Alice Johnson", 2018, true),
                        new Features(null, 1, 1, 2, true, false, false),
                        "A cozy and charming studio in the heart of downtown New York.",
                        List.of("WiFi", "Heating", "Kitchen"),
                        new Pricing(null, 125, 4.9),
                        new Availability(null, new Date(125, 6, 1), new Date(125, 6, 20))
                ),

                new AirBnbListing(null, "Modern Loft with City View", user,
                        new Location(null, "San Francisco", "CA", "USA"),
                        new ArrayList<>(),
                        imageService.randomHouseImage(),
                        new Host(null, "Mark Thompson", 2016, false),
                        new Features(null, 1, 1, 3, true, true, false),
                        "Spacious loft with stunning views of the city skyline.",
                        List.of("WiFi", "Fireplace", "Washer"),
                        new Pricing(null, 140, 4.8),
                        new Availability(null, new Date(125, 6, 5), new Date(125, 6, 25))
                ),

                new AirBnbListing(null, "Beachfront Bungalow Escape", user,
                        new Location(null, "Malibu", "CA", "USA"),
                        new ArrayList<>(),
                        imageService.randomHouseImage(),
                        new Host(null, "Sophie Nguyen", 2020, true),
                        new Features(null, 2, 1, 4, true, true, false),
                        "A beautiful beachfront bungalow perfect for romantic getaways.",
                        List.of("WiFi", "Fireplace", "Ocean View"),
                        new Pricing(null, 220, 4.95),
                        new Availability(null, new Date(125, 6, 10), new Date(125, 6, 30))
                ),

                new AirBnbListing(null, "Rustic Cabin in the Woods", user,
                        new Location(null, "Asheville", "NC", "USA"),
                        new ArrayList<>(),
                        imageService.randomHouseImage(),
                        new Host(null, "David Miller", 2015, false),
                        new Features(null, 3, 2, 6, false, true, true),
                        "Cozy log cabin nestled in the mountains with amazing views.",
                        List.of("Fireplace", "Mountain View", "Outdoor Grill"),
                        new Pricing(null, 180, 4.7),
                        new Availability(null, new Date(125, 5, 28), new Date(125, 6, 15))
                ),

                new AirBnbListing(null, "Luxury Penthouse with Terrace", user,
                        new Location(null, "Miami", "FL", "USA"),
                        new ArrayList<>(),
                        imageService.randomHouseImage(),
                        new Host(null, "Carla Diaz", 2019, true),
                        new Features(null, 2, 2, 5, true, false, false),
                        "Modern penthouse with private rooftop terrace and pool access.",
                        List.of("WiFi", "Pool", "Terrace"),
                        new Pricing(null, 350, 4.85),
                        new Availability(null, new Date(125, 6, 3), new Date(125, 6, 22))
                ),

                new AirBnbListing(null, "Countryside Farmhouse Retreat", user,
                        new Location(null, "Waco", "TX", "USA"),
                        new ArrayList<>(),
                        imageService.randomHouseImage(),
                        new Host(null, "Emma Lee", 2017, true),
                        new Features(null, 4, 2, 8, true, true, false),
                        "Rustic farmhouse surrounded by open fields and quiet serenity.",
                        List.of("WiFi", "Fireplace", "Garden"),
                        new Pricing(null, 160, 4.6),
                        new Availability(null, new Date(125, 6, 6), new Date(125, 6, 18))
                ),

                new AirBnbListing(null, "Tiny House in Nature", user,
                        new Location(null, "Boulder", "CO", "USA"),
                        new ArrayList<>(),
                        imageService.randomHouseImage(),
                        new Host(null, "Noah Carter", 2021, false),
                        new Features(null, 1, 1, 2, true, false, true),
                        "Minimalist eco-tiny house surrounded by forest and mountains.",
                        List.of("WiFi", "Mountain View", "Eco-Friendly"),
                        new Pricing(null, 95, 4.75),
                        new Availability(null, new Date(125, 6, 1), new Date(125, 6, 12))
                ),

                new AirBnbListing(null, "Scandinavian Home with Sauna", user,
                        new Location(null, "Seattle", "WA", "USA"),
                        new ArrayList<>(),
                        imageService.randomHouseImage(),
                        new Host(null, "Lena Berg", 2022, true),
                        new Features(null, 2, 1, 4, true, true, true),
                        "Scandinavian-inspired home with wood interior and private sauna.",
                        List.of("WiFi", "Fireplace", "Sauna"),
                        new Pricing(null, 210, 4.88),
                        new Availability(null, new Date(125, 6, 7), new Date(125, 6, 31))
                ),

                new AirBnbListing(null, "Desert Dome Retreat", user,
                        new Location(null, "Joshua Tree", "CA", "USA"),
                        new ArrayList<>(),
                        imageService.randomHouseImage(),
                        new Host(null, "Olivia Park", 2023, true),
                        new Features(null, 2, 1, 4, true, false, true),
                        "Geodesic dome in the desert perfect for stargazing.",
                        List.of("WiFi", "Eco-Friendly", "Stargazing Deck"),
                        new Pricing(null, 185, 4.92),
                        new Availability(null, new Date(125, 6, 2), new Date(125, 6, 28))
                ),

                new AirBnbListing(null, "Ski Chalet with Hot Tub", user,
                        new Location(null, "Aspen", "CO", "USA"),
                        new ArrayList<>(),
                        imageService.randomHouseImage(),
                        new Host(null, "Mason Hill", 2014, false),
                        new Features(null, 3, 2, 6, true, true, true),
                        "Luxury ski chalet with private hot tub and mountain views.",
                        List.of("WiFi", "Hot Tub", "Mountain View"),
                        new Pricing(null, 390, 4.9),
                        new Availability(null, new Date(125, 6, 8), new Date(125, 6, 29))
                )
        );

    }
}
