package com.abhimanyu.dogsitting.backend.testData;

import com.abhimanyu.dogsitting.backend.model.Booking;
import com.abhimanyu.dogsitting.backend.model.PetSize;
import com.abhimanyu.dogsitting.backend.model.ServiceType;
import com.abhimanyu.dogsitting.backend.model.BookingStatus;
import com.abhimanyu.dogsitting.backend.repository.BookingRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Profile("dev") // only runs when 'dev' profile is active
public class DemoDataLoader implements CommandLineRunner {

    private final BookingRepository bookingRepository;

    public DemoDataLoader(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public void run(String... args) {
            // Don't reseed if there is already data
            if (bookingRepository.count() > 0) {
                return;
            }

            LocalDateTime now = LocalDateTime.now();

            Booking b1 = new Booking();
            b1.setClientName("John Doe");
            b1.setClientEmail("john@example.com");
            b1.setPetName("Rex");
            b1.setPetSize(PetSize.MEDIUM);
            b1.setServiceType(ServiceType.HOUSE_SITTING);
            b1.setStartDate(LocalDate.of(2025, 12, 20));
            b1.setEndDate(LocalDate.of(2025, 12, 23));
            b1.setNotes("Friendly, good with other dogs.");
            b1.setStatus(BookingStatus.PENDING);
            b1.setTotalPrice(165.0);
            b1.setCreatedAt(now.minusDays(10));

            Booking b2 = new Booking();
            b2.setClientName("Jane Smith");
            b2.setClientEmail("jane@example.com");
            b2.setPetName("Luna");
            b2.setPetSize(PetSize.SMALL);
            b2.setServiceType(ServiceType.DROP_IN);
            b2.setStartDate(LocalDate.of(2025, 12, 24));
            b2.setEndDate(LocalDate.of(2025, 12, 27));
            b2.setNotes("Needs medication twice a day.");
            b2.setStatus(BookingStatus.CONFIRMED);
            b2.setTotalPrice(220.0);
            b2.setCreatedAt(now.minusDays(9));

            Booking b3 = new Booking();
            b3.setClientName("Alex Johnson");
            b3.setClientEmail("alex@example.com");
            b3.setPetName("Buddy");
            b3.setPetSize(PetSize.LARGE);
            b3.setServiceType(ServiceType.WALK);
            b3.setStartDate(LocalDate.of(2025, 12, 21));
            b3.setEndDate(LocalDate.of(2025, 12, 21));
            b3.setNotes("High energy, loves long walks.");
            b3.setStatus(BookingStatus.CONFIRMED);
            b3.setTotalPrice(45.0);
            b3.setCreatedAt(now.minusDays(8));

            Booking b4 = new Booking();
            b4.setClientName("Emily Davis");
            b4.setClientEmail("emily@example.com");
            b4.setPetName("Milo");
            b4.setPetSize(PetSize.SMALL);
            b4.setServiceType(ServiceType.HOUSE_SITTING);
            b4.setStartDate(LocalDate.of(2025, 12, 30));
            b4.setEndDate(LocalDate.of(2026, 1, 2));
            b4.setNotes("Shy at first but warms up quickly.");
            b4.setStatus(BookingStatus.PENDING);
            b4.setTotalPrice(300.0);
            b4.setCreatedAt(now.minusDays(7));

            Booking b5 = new Booking();
            b5.setClientName("Chris Lee");
            b5.setClientEmail("chris@example.com");
            b5.setPetName("Rocky");
            b5.setPetSize(PetSize.LARGE);
            b5.setServiceType(ServiceType.DROP_IN);
            b5.setStartDate(LocalDate.of(2026, 1, 5));
            b5.setEndDate(LocalDate.of(2026, 1, 7));
            b5.setNotes("Guard dog, nervous around strangers.");
            b5.setStatus(BookingStatus.PENDING);
            b5.setTotalPrice(180.0);
            b5.setCreatedAt(now.minusDays(6));

            Booking b6 = new Booking();
            b6.setClientName("Sarah Wilson");
            b6.setClientEmail("sarah@example.com");
            b6.setPetName("Coco");
            b6.setPetSize(PetSize.MEDIUM);
            b6.setServiceType(ServiceType.WALK);
            b6.setStartDate(LocalDate.of(2025, 11, 10));
            b6.setEndDate(LocalDate.of(2025, 11, 10));
            b6.setNotes("Senior dog, short walks only.");
            b6.setStatus(BookingStatus.CONFIRMED);
            b6.setTotalPrice(30.0);
            b6.setCreatedAt(now.minusDays(30));

            Booking b7 = new Booking();
            b7.setClientName("Michael Brown");
            b7.setClientEmail("michael@example.com");
            b7.setPetName("Shadow");
            b7.setPetSize(PetSize.LARGE);
            b7.setServiceType(ServiceType.HOUSE_SITTING);
            b7.setStartDate(LocalDate.of(2025, 11, 15));
            b7.setEndDate(LocalDate.of(2025, 11, 18));
            b7.setNotes("Requires crate at night.");
            b7.setStatus(BookingStatus.CONFIRMED);
            b7.setTotalPrice(250.0);
            b7.setCreatedAt(now.minusDays(28));

            Booking b8 = new Booking();
            b8.setClientName("Olivia Martinez");
            b8.setClientEmail("olivia@example.com");
            b8.setPetName("Bella");
            b8.setPetSize(PetSize.SMALL);
            b8.setServiceType(ServiceType.DROP_IN);
            b8.setStartDate(LocalDate.of(2025, 11, 20));
            b8.setEndDate(LocalDate.of(2025, 11, 22));
            b8.setNotes("Needs eye drops once a day.");
            b8.setStatus(BookingStatus.CONFIRMED);
            b8.setTotalPrice(140.0);
            b8.setCreatedAt(now.minusDays(25));

            Booking b9 = new Booking();
            b9.setClientName("David Clark");
            b9.setClientEmail("david@example.com");
            b9.setPetName("Max");
            b9.setPetSize(PetSize.MEDIUM);
            b9.setServiceType(ServiceType.WALK);
            b9.setStartDate(LocalDate.of(2025, 12, 1));
            b9.setEndDate(LocalDate.of(2025, 12, 1));
            b9.setNotes("Good off-leash in enclosed areas.");
            b9.setStatus(BookingStatus.CONFIRMED);
            b9.setTotalPrice(35.0);
            b9.setCreatedAt(now.minusDays(14));

            Booking b10 = new Booking();
            b10.setClientName("Sophia Hernandez");
            b10.setClientEmail("sophia@example.com");
            b10.setPetName("Nala");
            b10.setPetSize(PetSize.SMALL);
            b10.setServiceType(ServiceType.HOUSE_SITTING);
            b10.setStartDate(LocalDate.of(2025, 12, 10));
            b10.setEndDate(LocalDate.of(2025, 12, 12));
            b10.setNotes("Prefers to sleep on the bed.");
            b10.setStatus(BookingStatus.CONFIRMED);
            b10.setTotalPrice(190.0);
            b10.setCreatedAt(now.minusDays(12));

            Booking b11 = new Booking();
            b11.setClientName("James Walker");
            b11.setClientEmail("james@example.com");
            b11.setPetName("Zeus");
            b11.setPetSize(PetSize.LARGE);
            b11.setServiceType(ServiceType.DROP_IN);
            b11.setStartDate(LocalDate.of(2025, 12, 18));
            b11.setEndDate(LocalDate.of(2025, 12, 19));
            b11.setNotes("Strong leash puller, use harness.");
            b11.setStatus(BookingStatus.PENDING);
            b11.setTotalPrice(95.0);
            b11.setCreatedAt(now.minusDays(5));

            Booking b12 = new Booking();
            b12.setClientName("Ava Thompson");
            b12.setClientEmail("ava@example.com");
            b12.setPetName("Daisy");
            b12.setPetSize(PetSize.MEDIUM);
            b12.setServiceType(ServiceType.WALK);
            b12.setStartDate(LocalDate.of(2025, 12, 22));
            b12.setEndDate(LocalDate.of(2025, 12, 22));
            b12.setNotes("Puppy in training, bring treats.");
            b12.setStatus(BookingStatus.PENDING);
            b12.setTotalPrice(40.0);
            b12.setCreatedAt(now.minusDays(3));

        // ---------- EDGE CASES ----------

        // b13 + b14: Overlapping bookings for the SAME dog (Rex) on overlapping dates
        Booking b13 = new Booking();
        b13.setClientName("John Doe");
        b13.setClientEmail("john@example.com");
        b13.setPetName("Rex");
        b13.setPetSize(PetSize.MEDIUM);
        b13.setServiceType(ServiceType.WALK);
        b13.setStartDate(LocalDate.of(2025, 12, 21));
        b13.setEndDate(LocalDate.of(2025, 12, 22));
        b13.setNotes("Midday walk while house sitting.");
        b13.setStatus(BookingStatus.CONFIRMED);
        b13.setTotalPrice(60.0);
        b13.setCreatedAt(now.minusDays(9));

        Booking b14 = new Booking();
        b14.setClientName("John Doe");
        b14.setClientEmail("john@example.com");
        b14.setPetName("Rex");
        b14.setPetSize(PetSize.MEDIUM);
        b14.setServiceType(ServiceType.DROP_IN);
        b14.setStartDate(LocalDate.of(2025, 12, 22));
        b14.setEndDate(LocalDate.of(2025, 12, 23));
        b14.setNotes("Extra drop-ins requested.");
        b14.setStatus(BookingStatus.PENDING);
        b14.setTotalPrice(80.0);
        b14.setCreatedAt(now.minusDays(8));

        // b15: Back-to-back with b4 (end date = next start date)
        Booking b15 = new Booking();
        b15.setClientName("Emily Davis");
        b15.setClientEmail("emily@example.com");
        b15.setPetName("Milo");
        b15.setPetSize(PetSize.SMALL);
        b15.setServiceType(ServiceType.WALK);
        b15.setStartDate(LocalDate.of(2026, 1, 2)); // same as b4 end date
        b15.setEndDate(LocalDate.of(2026, 1, 2));
        b15.setNotes("Final walk before owner returns.");
        b15.setStatus(BookingStatus.CONFIRMED);
        b15.setTotalPrice(35.0);
        b15.setCreatedAt(now.minusDays(6));

        // b16: Long booking (multi-week house sitting)
        Booking b16 = new Booking();
        b16.setClientName("Long Trip Family");
        b16.setClientEmail("trip@example.com");
        b16.setPetName("Maple");
        b16.setPetSize(PetSize.MEDIUM);
        b16.setServiceType(ServiceType.HOUSE_SITTING);
        b16.setStartDate(LocalDate.of(2026, 2, 1));
        b16.setEndDate(LocalDate.of(2026, 2, 21)); // 3 weeks
        b16.setNotes("Family on extended vacation.");
        b16.setStatus(BookingStatus.PENDING);
        b16.setTotalPrice(2100.0);
        b16.setCreatedAt(now.minusDays(1));

        // b17: Zero-price booking (free walk / test)
        Booking b17 = new Booking();
        b17.setClientName("Test Client");
        b17.setClientEmail("test@example.com");
        b17.setPetName("Test Dog");
        b17.setPetSize(PetSize.SMALL);
        b17.setServiceType(ServiceType.WALK);
        b17.setStartDate(LocalDate.of(2025, 12, 5));
        b17.setEndDate(LocalDate.of(2025, 12, 5));
        b17.setNotes("Free trial walk.");
        b17.setStatus(BookingStatus.CONFIRMED);
        b17.setTotalPrice(0.0);
        b17.setCreatedAt(now.minusDays(15));

        // b18: No notes + same-day booking in the past
        Booking b18 = new Booking();
        b18.setClientName("No Notes Client");
        b18.setClientEmail("nonotes@example.com");
        b18.setPetName("Ghost");
        b18.setPetSize(PetSize.LARGE);
        b18.setServiceType(ServiceType.DROP_IN);
        b18.setStartDate(LocalDate.of(2025, 10, 10));
        b18.setEndDate(LocalDate.of(2025, 10, 10));
        // no notes set (null)
        b18.setStatus(BookingStatus.CONFIRMED);
        b18.setTotalPrice(25.0);
        b18.setCreatedAt(now.minusDays(60));


            bookingRepository.saveAll(
                    List.of(
                            b1, b2, b3, b4, b5, b6,
                            b7, b8, b9, b10, b11, b12,
                            b13, b14, b15, b16, b17, b18
                    )
            );
    }
}
