package com.abhimanyu.dogsitting.backend.service;

import com.abhimanyu.dogsitting.backend.dto.BookingResponse;
import com.abhimanyu.dogsitting.backend.exception.BookingNotFoundException;
import com.abhimanyu.dogsitting.backend.model.Booking;
import com.abhimanyu.dogsitting.backend.model.BookingStatus;
import com.abhimanyu.dogsitting.backend.repository.BookingRepository;
import com.abhimanyu.dogsitting.backend.dto.BookingRequest;
import com.abhimanyu.dogsitting.backend.model.PetSize;
import com.abhimanyu.dogsitting.backend.model.ServiceType;
import com.abhimanyu.dogsitting.backend.dto.PriceEstimateRequest;
import com.abhimanyu.dogsitting.backend.dto.PriceEstimateResponse;
import com.abhimanyu.dogsitting.backend.dto.BookingStatsResponse;


import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


public class BookingServiceImplTest {

    private final BookingRepository repo = mock(BookingRepository.class);
    private final BookingServiceImpl service = new BookingServiceImpl(repo);

    @Test
    void getBookingById_returnsBookingResponse_whenFound() {
        // Arrange
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setClientName("John Doe");
        booking.setClientEmail("john@example.com");
        booking.setPetName("Rex");
        booking.setPetSize(null);           // or a real value if you want
        booking.setServiceType(null);       // same here
        booking.setStartDate(LocalDate.now());
        booking.setEndDate(LocalDate.now().plusDays(3));
        booking.setNotes("Test booking");
        booking.setStatus(BookingStatus.PENDING);
        booking.setCreatedAt(LocalDateTime.now());
        booking.setTotalPrice(123.45);

        when(repo.findById(1L)).thenReturn(Optional.of(booking));

        // Act
        BookingResponse response = service.getBookingById(1L);

        // Assert
        assertNotNull(response);
        // If BookingResponse is a record, you’d use: response.id()
        // If it's a class with getters, you’d use: response.getId()
        // To keep it generic, we just assert it's not null and repo was used.
        verify(repo).findById(1L);
    }
    @Test
    void getBookingById_throwsBookingNotFoundException_whenMissing() {
        // Arrange
        when(repo.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(BookingNotFoundException.class,
                () -> service.getBookingById(99L));

        verify(repo).findById(99L);
    }

    @Test
    void updateBookingStatus_allowsPendingToConfirmed_andSaves() {
        // Arrange
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStatus(BookingStatus.PENDING);

        when(repo.findById(1L)).thenReturn(Optional.of(booking));
        // echo back whatever we save so service gets an updated instance
        when(repo.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        BookingResponse response = service.updateBookingStatus(1L, BookingStatus.CONFIRMED);

        // Assert
        assertNotNull(response);

        ArgumentCaptor<Booking> captor = ArgumentCaptor.forClass(Booking.class);
        verify(repo).save(captor.capture());
        Booking saved = captor.getValue();

        assertEquals(BookingStatus.CONFIRMED, saved.getStatus());
    }

    @Test
    void updateBookingStatus_rejectsInvalidTransitionFromCanceled() {
        // Arrange
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStatus(BookingStatus.CANCELED);

        when(repo.findById(1L)).thenReturn(Optional.of(booking));

        // Act + Assert
        assertThrows(IllegalStateException.class,
                () -> service.updateBookingStatus(1L, BookingStatus.CONFIRMED));

        // Ensure we never try to save when transition is invalid
        verify(repo, never()).save(any());
    }
    @Test
    void createBooking_setsDefaultsAndCalculatesPrice_forHouseSitting() {
        // Arrange
        BookingRequest request = new BookingRequest();
        request.setClientName("John Doe");
        request.setClientEmail("john@example.com");
        request.setPetName("Rex");
        request.setPetSize(PetSize.MEDIUM);
        request.setServiceType(ServiceType.HOUSE_SITTING);

        LocalDate start = LocalDate.of(2025, 12, 20);
        LocalDate end = LocalDate.of(2025, 12, 23); // 3 days difference
        request.setStartDate(start);
        request.setEndDate(end);
        request.setNotes("Friendly, good with other dogs.");

        // repo.save should just return the same booking with an id set
        when(repo.save(any(Booking.class))).thenAnswer(invocation -> {
            Booking b = invocation.getArgument(0);
            b.setId(1L);
            return b;
        });

        double expectedPrice = 55.0 * 3; // HOUSE_SITTING base per night * 3 days

        // Act
        BookingResponse response = service.createBooking(request);

        // Assert
        ArgumentCaptor<Booking> captor = ArgumentCaptor.forClass(Booking.class);
        verify(repo).save(captor.capture());
        Booking saved = captor.getValue();

        assertEquals("John Doe", saved.getClientName());
        assertEquals("john@example.com", saved.getClientEmail());
        assertEquals("Rex", saved.getPetName());
        assertEquals(PetSize.MEDIUM, saved.getPetSize());
        assertEquals(ServiceType.HOUSE_SITTING, saved.getServiceType());
        assertEquals(start, saved.getStartDate());
        assertEquals(end, saved.getEndDate());
        assertEquals("Friendly, good with other dogs.", saved.getNotes());

        // default status + createdAt
        assertEquals(BookingStatus.PENDING, saved.getStatus());
        assertNotNull(saved.getCreatedAt());

        // price logic
        assertEquals(expectedPrice, saved.getTotalPrice(), 0.0001);

        // (Optional) basic checks on response as well
        assertNotNull(response);
        // If BookingResponse is a record: response.totalPrice()
        // If it's a class: response.getTotalPrice()
    }
    @Test
    void createBooking_usesMinimumOneDay_whenStartAndEndSame_forWalk() {
        // Arrange
        BookingRequest request = new BookingRequest();
        request.setClientName("Same Day Client");
        request.setClientEmail("same@example.com");
        request.setPetName("Bolt");
        request.setPetSize(PetSize.SMALL);
        request.setServiceType(ServiceType.WALK);

        LocalDate date = LocalDate.of(2025, 12, 20);
        request.setStartDate(date);
        request.setEndDate(date); // same day → should count as 1 day

        request.setNotes("Quick walk.");

        when(repo.save(any(Booking.class))).thenAnswer(invocation -> {
            Booking b = invocation.getArgument(0);
            b.setId(2L);
            return b;
        });

        double expectedPrice = 20.0 * 1; // WALK base per "day" (here, min 1)

        // Act
        BookingResponse response = service.createBooking(request);

        // Assert
        ArgumentCaptor<Booking> captor = ArgumentCaptor.forClass(Booking.class);
        verify(repo).save(captor.capture());
        Booking saved = captor.getValue();

        assertEquals(ServiceType.WALK, saved.getServiceType());
        assertEquals(expectedPrice, saved.getTotalPrice(), 0.0001);
        assertEquals(BookingStatus.PENDING, saved.getStatus());
        assertNotNull(saved.getCreatedAt());

        assertNotNull(response);
    }
    @Test
    void deleteBooking_deletesWhenBookingExists() {
        // Arrange
        Booking booking = new Booking();
        booking.setId(1L);

        when(repo.findById(1L)).thenReturn(Optional.of(booking));

        // Act
        service.deleteBooking(1L);

        // Assert
        verify(repo).findById(1L);
        verify(repo).delete(booking);
    }
    @Test
    void deleteBooking_throwsBookingNotFoundException_whenMissing() {
        // Arrange
        when(repo.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(BookingNotFoundException.class,
                () -> service.deleteBooking(99L));

        verify(repo).findById(99L);
        verify(repo, never()).delete(any());
    }

    @Test
    void updateBooking_updatesFieldsAndRecalculatesPrice_whenBookingExists() {
        // Arrange existing booking
        Booking existing = new Booking();
        existing.setId(1L);
        existing.setClientName("Old Name");
        existing.setClientEmail("old@example.com");
        existing.setPetName("Old Pet");
        existing.setPetSize(PetSize.SMALL);
        existing.setServiceType(ServiceType.WALK);
        existing.setStartDate(LocalDate.of(2025, 12, 20));
        existing.setEndDate(LocalDate.of(2025, 12, 21));
        existing.setNotes("Old notes");
        existing.setStatus(BookingStatus.PENDING);
        existing.setCreatedAt(LocalDateTime.now());
        existing.setTotalPrice(20.0); // old price

        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // New data from client
        BookingRequest request = new BookingRequest();
        request.setClientName("New Name");
        request.setClientEmail("new@example.com");
        request.setPetName("New Pet");
        request.setPetSize(PetSize.LARGE);
        request.setServiceType(ServiceType.HOUSE_SITTING);
        request.setStartDate(LocalDate.of(2025, 12, 20));
        request.setEndDate(LocalDate.of(2025, 12, 23)); // 3 days
        request.setNotes("Updated notes");

        double expectedPrice = 55.0 * 3; // HOUSE_SITTING base per night * 3 days

        // Act
        BookingResponse response = service.updateBooking(1L, request);

        // Assert
        assertNotNull(response);

        ArgumentCaptor<Booking> captor = ArgumentCaptor.forClass(Booking.class);
        verify(repo).save(captor.capture());
        Booking saved = captor.getValue();

        // verify updated fields
        assertEquals("New Name", saved.getClientName());
        assertEquals("new@example.com", saved.getClientEmail());
        assertEquals("New Pet", saved.getPetName());
        assertEquals(PetSize.LARGE, saved.getPetSize());
        assertEquals(ServiceType.HOUSE_SITTING, saved.getServiceType());
        assertEquals(LocalDate.of(2025, 12, 20), saved.getStartDate());
        assertEquals(LocalDate.of(2025, 12, 23), saved.getEndDate());
        assertEquals("Updated notes", saved.getNotes());

        // verify status & createdAt preserved
        assertEquals(BookingStatus.PENDING, saved.getStatus());
        assertNotNull(saved.getCreatedAt());

        // verify price recalculated
        assertEquals(expectedPrice, saved.getTotalPrice(), 0.0001);
    }
    @Test
    void updateBooking_throwsBookingNotFoundException_whenMissing() {
        // Arrange
        when(repo.findById(99L)).thenReturn(Optional.empty());

        BookingRequest request = new BookingRequest();
        // we don't care about the contents here since it should fail before using it

        // Act + Assert
        assertThrows(BookingNotFoundException.class,
                () -> service.updateBooking(99L, request));

        verify(repo).findById(99L);
        verify(repo, never()).save(any());
    }

    @Test
    void searchBookings_noFilters_returnsAllBookings() {
        // Arrange
        Booking b1 = new Booking();
        b1.setId(1L);
        Booking b2 = new Booking();
        b2.setId(2L);

        when(repo.findAll()).thenReturn(List.of(b1, b2));

        // Act
        var result = service.searchBookings(null, null);

        // Assert
        assertEquals(2, result.size());
        verify(repo).findAll();
        verify(repo, never()).findByStatus(any());
        verify(repo, never()).findByClientEmailIgnoreCase(anyString());
        verify(repo, never()).findByStatusAndClientEmailIgnoreCase(any(), anyString());
    }

    @Test
    void searchBookings_withStatusOnly_usesFindByStatus() {
        // Arrange
        Booking b1 = new Booking();
        b1.setId(1L);
        b1.setStatus(BookingStatus.PENDING);

        when(repo.findByStatus(BookingStatus.PENDING)).thenReturn(List.of(b1));

        // Act
        var result = service.searchBookings(BookingStatus.PENDING, null);

        // Assert
        assertEquals(1, result.size());
        verify(repo).findByStatus(BookingStatus.PENDING);
        verify(repo, never()).findAll();
        verify(repo, never()).findByClientEmailIgnoreCase(anyString());
        verify(repo, never()).findByStatusAndClientEmailIgnoreCase(any(), anyString());
    }

    @Test
    void searchBookings_withEmailOnly_usesFindByClientEmailIgnoreCase() {
        // Arrange
        Booking b1 = new Booking();
        b1.setId(1L);
        b1.setClientEmail("john@example.com");

        when(repo.findByClientEmailIgnoreCase("john@example.com"))
                .thenReturn(List.of(b1));

        // Act
        var result = service.searchBookings(null, "john@example.com");

        // Assert
        assertEquals(1, result.size());
        verify(repo).findByClientEmailIgnoreCase("john@example.com");
        verify(repo, never()).findAll();
        verify(repo, never()).findByStatus(any());
        verify(repo, never()).findByStatusAndClientEmailIgnoreCase(any(), anyString());
    }

    @Test
    void searchBookings_withStatusAndEmail_usesCombinedQuery() {
        // Arrange
        Booking b1 = new Booking();
        b1.setId(1L);
        b1.setStatus(BookingStatus.CONFIRMED);
        b1.setClientEmail("john@example.com");

        when(repo.findByStatusAndClientEmailIgnoreCase(BookingStatus.CONFIRMED, "john@example.com"))
                .thenReturn(List.of(b1));

        // Act
        var result = service.searchBookings(BookingStatus.CONFIRMED, "john@example.com");

        // Assert
        assertEquals(1, result.size());
        verify(repo).findByStatusAndClientEmailIgnoreCase(BookingStatus.CONFIRMED, "john@example.com");
        verify(repo, never()).findAll();
        verify(repo, never()).findByStatus(any());
        verify(repo, never()).findByClientEmailIgnoreCase(anyString());
    }
    @Test
    void estimatePrice_calculatesCorrectly_forHouseSittingMultiDay() {
        // Arrange
        PriceEstimateRequest request = new PriceEstimateRequest();
        request.setServiceType(ServiceType.HOUSE_SITTING);
        request.setPetSize(PetSize.MEDIUM);
        request.setStartDate(LocalDate.of(2025, 12, 20));
        request.setEndDate(LocalDate.of(2025, 12, 23)); // 3 days

        double expected = 55.0 * 3;

        // Act
        PriceEstimateResponse response = service.estimatePrice(request);

        // Assert
        assertNotNull(response);
        assertEquals(expected, response.getEstimatedPrice(), 0.0001);

        // no repository interaction expected
        verifyNoInteractions(repo);
    }

    @Test
    void estimatePrice_usesMinimumOneDay_forSameDayWalk() {
        // Arrange
        PriceEstimateRequest request = new PriceEstimateRequest();
        request.setServiceType(ServiceType.WALK);
        request.setPetSize(PetSize.SMALL);
        LocalDate date = LocalDate.of(2025, 12, 20);
        request.setStartDate(date);
        request.setEndDate(date); // same day

        double expected = 20.0 * 1; // WALK base, min 1 day

        // Act
        PriceEstimateResponse response = service.estimatePrice(request);

        // Assert
        assertNotNull(response);
        assertEquals(expected, response.getEstimatedPrice(), 0.0001);
        verifyNoInteractions(repo);
    }

    @Test
    void getStats_returnsCorrectCounts() {
        // Arrange
        when(repo.count()).thenReturn(10L);
        when(repo.countByStatus(BookingStatus.PENDING)).thenReturn(4L);
        when(repo.countByStatus(BookingStatus.CONFIRMED)).thenReturn(3L);
        when(repo.countByStatus(BookingStatus.CANCELED)).thenReturn(3L);

        // Act
        BookingStatsResponse result = service.getStats();

        // Assert
        assertNotNull(result);
        assertEquals(10L, result.getTotal());
        assertEquals(4L, result.getPending());
        assertEquals(3L, result.getConfirmed());
        assertEquals(3L, result.getCanceled());

        verify(repo).count();
        verify(repo).countByStatus(BookingStatus.PENDING);
        verify(repo).countByStatus(BookingStatus.CONFIRMED);
        verify(repo).countByStatus(BookingStatus.CANCELED);
    }



}