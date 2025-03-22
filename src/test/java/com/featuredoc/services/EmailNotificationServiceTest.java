package com.featuredoc.services;

import com.featuredoc.dto.FeatureRequest;
import com.featuredoc.models.*;
import com.featuredoc.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
class EmailNotificationServiceTest {

    @Mock
    private JavaMailSender emailSender;

    @Mock
    private EmailService emailService;

    @Mock
    private UserService userService;

    @InjectMocks
    private EmailNotificationService emailNotificationService;

    @Test
    public void getNotificationRecipientsBestCase() {
        long previouslyAssigned = 1L;
        long newlyAssigned = 2L;

        User previouslyAssignedUser = new User(previouslyAssigned, "name", "email");
        User newlyAssignedUser = new User(newlyAssigned, "name 2", "email 2");

        when(userService.getUserById(previouslyAssigned))
                .thenReturn(Optional.of(previouslyAssignedUser));

        when(userService.getUserById(newlyAssigned))
                .thenReturn(Optional.of(newlyAssignedUser));

        List<String> recipients = List.of(
                previouslyAssignedUser.getEmail(),
                newlyAssignedUser.getEmail()
        );

        List<String> actual = emailNotificationService.getNotificationRecipients(newlyAssigned, previouslyAssigned);

        assertEquals(recipients, actual);
    }

    @Test
    public void getNotificationRecipientsOneUserNotExist() {
        long previouslyAssigned = 1L;
        long newlyAssigned = -2L;

        Optional<User> previouslyAssignedUser = Optional.of(new User(previouslyAssigned, "name", "email"));
        Optional<User> newlyAssignedUser = Optional.empty();

        when(userService.getUserById(previouslyAssigned))
                .thenReturn(previouslyAssignedUser);

        when(userService.getUserById(newlyAssigned))
                .thenReturn(newlyAssignedUser);

        assertThrows(NoSuchElementException.class, () -> emailNotificationService.getNotificationRecipients(newlyAssigned, previouslyAssigned));

    }

    @Test
    public void getNotificationRecipientsBothUsersNotExist() {
        long previouslyAssigned = -1L;
        long newlyAssigned = -2L;

        Optional<User> previouslyAssignedUser = Optional.empty();
        Optional<User> newlyAssignedUser = Optional.empty();

        when(userService.getUserById(previouslyAssigned))
                .thenReturn(previouslyAssignedUser);

        when(userService.getUserById(newlyAssigned))
                .thenReturn(newlyAssignedUser);

        assertThrows(NoSuchElementException.class, () -> emailNotificationService.getNotificationRecipients(newlyAssigned, previouslyAssigned));

    }

    @Test
    public void sendUpdateEmailBestCase() {
        FeatureRequest featureRequest = new FeatureRequest(1L, 1L, 1L, 1, 1, 1L, "name", "description", "url");
        FeatureVersion featureVersion = new FeatureVersion(1L, 1L, 1L, 1, 1, 1L, "nameOld", "description",Timestamp.from(Instant.now()), null, "url");
        Priority priority = new Priority(1, "1");
        FeatureStatus featureStatus = new FeatureStatus(1, "1");
        List<String> recipients = List.of("email1", "email2");
        User user = new User(1L, "name", "email1");

        doNothing().when(emailService).sendSimpleMessage(anyList(), anyString(), anyString());

        emailNotificationService.sendUpdateEmail(featureRequest, featureVersion, priority, featureStatus, recipients, user);

        verify(emailService, times(1)).sendSimpleMessage(anyList(), anyString(), anyString());
    }

    @Test
    public void sendUpdateEmailNoRecipients() {
        FeatureRequest featureRequest = new FeatureRequest(1L, 1L, 1L, 1, 1, 1L, "name", "description", "url");
        FeatureVersion featureVersion = new FeatureVersion(1L, 1L, 1L, 1, 1, 1L, "nameOld", "description",Timestamp.from(Instant.now()), null, "url");
        Priority priority = new Priority(1, "1");
        FeatureStatus featureStatus = new FeatureStatus(1, "1");
        List<String> recipients = List.of();
        User user = new User(1L, "name", "email1");

        doNothing().when(emailService).sendSimpleMessage(anyList(), anyString(), anyString());

        assertThrows(IllegalArgumentException.class, () -> emailNotificationService.sendUpdateEmail(featureRequest, featureVersion, priority, featureStatus, recipients, user));
        verify(emailService, never()).sendSimpleMessage(anyList(), anyString(), anyString());
    }

}