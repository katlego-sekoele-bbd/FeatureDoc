package com.featuredoc.services;

import com.featuredoc.dto.FeatureRequest;
import com.featuredoc.models.*;
import com.featuredoc.repository.FeatureRepository;
import com.featuredoc.repository.FeatureStatusRepository;
import com.featuredoc.repository.FeatureVersionRepository;
import com.featuredoc.repository.PriorityRepository;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
class FeatureServiceTest {

    @Mock
    private FeatureVersionRepository featureVersionRepository;

    @Mock
    private UserService userService;

    @Mock
    private FeatureViewService Service;

    @Mock
    private FeatureStatusService featureStatusService;

    @Mock
    private PriorityService priorityService;

    @Mock
    private PriorityRepository priorityRepository;

    @Mock
    private FeatureRepository featureRepository;

    @Mock
    private FeatureStatusRepository featureStatusRepository;

    @Mock
    private FeatureVersionService featureVersionService;

    @Mock
    private EmailNotificationService emailNotificationService;

    @Spy
    @InjectMocks
    private FeatureService featureService;

    public void setUpMockAuthentication(String name, String email, List<String> grantedAuthorities, boolean isAuthenticated) {
        Authentication authentication =
                new Authentication() {
                    @Override
                    public Collection<? extends GrantedAuthority> getAuthorities() {
                        List<GrantedAuthority> authorities = new ArrayList<>();
                        grantedAuthorities.forEach(authority -> authorities.add(new SimpleGrantedAuthority(authority)));
                        return authorities;
                    }

                    @Override
                    public Object getCredentials() {
                        return null;
                    }

                    @Override
                    public Object getDetails() {
                        return null;
                    }

                    @Override
                    public Object getPrincipal() {
                        return email;
                    }

                    @Override
                    public boolean isAuthenticated() {
                        return isAuthenticated;
                    }

                    @Override
                    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

                    }

                    @Override
                    public String getName() {
                        return name;
                    }
                };

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    public void addFeatureBestCase() {

        // Mock input request
        FeatureRequest request = new FeatureRequest();
        request.setFeatureStatusID(1);
        request.setPriorityID(2);
        request.setAssignedTo(2L);
        request.setName("New Feature");
        request.setShortDescription("Short Desc");
        request.setURL("http://example.com");

        // Mock return values for dependencies
        User mockUser = new User();
        mockUser.setUserID(1L);
        when(userService.getCurrentUser()).thenReturn(mockUser);

        Feature mockFeature = new Feature(mockUser.getUserID());
        mockFeature.setFeatureID(1L);
        when(featureRepository.save(any(Feature.class))).thenReturn(mockFeature);

        FeatureStatus mockStatus = new FeatureStatus();
        when(featureStatusRepository.findById(1L)).thenReturn(Optional.of(mockStatus));
        when(featureStatusService.getFeatureStatusById(1L)).thenReturn(Optional.of(mockStatus));

        Priority mockPriority = new Priority();
        when(priorityRepository.findById(2L)).thenReturn(Optional.of(mockPriority));
        when(priorityService.getPriorityById(2L)).thenReturn(Optional.of(mockPriority));

        User assignedUser = new User();
        assignedUser.setUserID(2L);
        when(userService.getUserById(2L)).thenReturn(Optional.of(assignedUser));

        // Mock email notification recipients
        List<String> recipients = List.of("user@example.com");
        when(emailNotificationService.getNotificationRecipients(2L, null)).thenReturn(recipients);

        // Execute the method
        Feature result = featureService.addFeature(request);

        // Verify interactions and assertions
        assertNotNull(result);
        assertEquals(1L, result.getFeatureID());

        verify(featureRepository, times(1)).save(any(Feature.class));
        verify(featureVersionRepository, times(1)).save(any(FeatureVersion.class));
        verify(emailNotificationService, times(1)).getNotificationRecipients(2L, null);
        verify(emailNotificationService, times(1)).sendUpdateEmail(eq(request), any(FeatureVersion.class),
                eq(mockPriority), eq(mockStatus), eq(recipients), eq(assignedUser));
    }

    @Test
    public void addFeatureNoCurrentUser() {

        // Mock input request
        FeatureRequest request = new FeatureRequest();
        request.setFeatureStatusID(1);
        request.setPriorityID(2);
        request.setAssignedTo(2L);
        request.setName("New Feature");
        request.setShortDescription("Short Desc");
        request.setURL("http://example.com");

        // Mock return values for dependencies
        User mockUser = new User();
        mockUser.setUserID(1L);
        when(userService.getCurrentUser()).thenThrow(new AuthenticationException("User not authenticated") {});

        Feature mockFeature = new Feature(mockUser.getUserID());
        mockFeature.setFeatureID(1L);
        when(featureRepository.save(any(Feature.class))).thenReturn(mockFeature);

        FeatureStatus mockStatus = new FeatureStatus();
        when(featureStatusRepository.findById(1L)).thenReturn(Optional.of(mockStatus));
        when(featureStatusService.getFeatureStatusById(1L)).thenReturn(Optional.of(mockStatus));

        Priority mockPriority = new Priority();
        when(priorityRepository.findById(2L)).thenReturn(Optional.of(mockPriority));
        when(priorityService.getPriorityById(2L)).thenReturn(Optional.of(mockPriority));

        User assignedUser = new User();
        assignedUser.setUserID(2L);
        when(userService.getUserById(2L)).thenReturn(Optional.of(assignedUser));

        // Mock email notification recipients
        List<String> recipients = List.of("user@example.com");
        when(emailNotificationService.getNotificationRecipients(2L, null)).thenReturn(recipients);

        // Verify interactions and assertions
        assertThrows(AuthenticationException.class, () -> featureService.addFeature(request));

        verify(featureRepository, times(0)).save(any(Feature.class));
        verify(featureVersionRepository, times(0)).save(any(FeatureVersion.class));
        verify(emailNotificationService, times(0)).getNotificationRecipients(2L, null);
        verify(emailNotificationService, times(0)).sendUpdateEmail(eq(request), any(FeatureVersion.class),
                eq(mockPriority), eq(mockStatus), eq(recipients), eq(assignedUser));
    }

    @Test
    public void addFeatureNoPriority() {

        // Mock input request
        FeatureRequest request = new FeatureRequest();
        request.setFeatureStatusID(1);
        request.setPriorityID(2);
        request.setAssignedTo(2L);
        request.setName("New Feature");
        request.setShortDescription("Short Desc");
        request.setURL("http://example.com");

        // Mock return values for dependencies
        User mockUser = new User();
        mockUser.setUserID(1L);
        when(userService.getCurrentUser()).thenReturn(mockUser);

        Feature mockFeature = new Feature(mockUser.getUserID());
        mockFeature.setFeatureID(1L);
        when(featureRepository.save(any(Feature.class))).thenReturn(mockFeature);

        FeatureStatus mockStatus = new FeatureStatus();
        when(featureStatusRepository.findById(1L)).thenReturn(Optional.of(mockStatus));
        when(featureStatusService.getFeatureStatusById(1L)).thenReturn(Optional.of(mockStatus));

        Priority mockPriority = new Priority();
        when(priorityRepository.findById(2L)).thenReturn(Optional.empty());
        when(priorityService.getPriorityById(2L)).thenReturn(Optional.empty());

        User assignedUser = new User();
        assignedUser.setUserID(2L);
        when(userService.getUserById(2L)).thenReturn(Optional.of(assignedUser));

        // Mock email notification recipients
        List<String> recipients = List.of("user@example.com");
        when(emailNotificationService.getNotificationRecipients(2L, null)).thenReturn(recipients);

        // Execute the method
        assertThrows(IllegalArgumentException.class, () -> featureService.addFeature(request));

        // Verify interactions and assertions
        verify(featureRepository, times(0)).save(any(Feature.class));
        verify(featureVersionRepository, times(0)).save(any(FeatureVersion.class));
        verify(emailNotificationService, times(0)).getNotificationRecipients(2L, null);
        verify(emailNotificationService, times(0)).sendUpdateEmail(eq(request), any(FeatureVersion.class),
                eq(mockPriority), eq(mockStatus), eq(recipients), eq(assignedUser));
    }

    @Test
    public void addFeatureNoFeatureStatus() {

        // Mock input request
        FeatureRequest request = new FeatureRequest();
        request.setFeatureStatusID(1);
        request.setPriorityID(2);
        request.setAssignedTo(2L);
        request.setName("New Feature");
        request.setShortDescription("Short Desc");
        request.setURL("http://example.com");

        // Mock return values for dependencies
        User mockUser = new User();
        mockUser.setUserID(1L);
        when(userService.getCurrentUser()).thenReturn(mockUser);

        Feature mockFeature = new Feature(mockUser.getUserID());
        mockFeature.setFeatureID(1L);
        when(featureRepository.save(any(Feature.class))).thenReturn(mockFeature);

        FeatureStatus mockStatus = new FeatureStatus();
        when(featureStatusRepository.findById(1L)).thenReturn(Optional.empty());
        when(featureStatusService.getFeatureStatusById(1L)).thenReturn(Optional.empty());

        Priority mockPriority = new Priority();
        when(priorityRepository.findById(2L)).thenReturn(Optional.of(mockPriority));
        when(priorityService.getPriorityById(2L)).thenReturn(Optional.of(mockPriority));

        User assignedUser = new User();
        assignedUser.setUserID(2L);
        when(userService.getUserById(2L)).thenReturn(Optional.of(assignedUser));

        // Mock email notification recipients
        List<String> recipients = List.of("user@example.com");
        when(emailNotificationService.getNotificationRecipients(2L, null)).thenReturn(recipients);

        // Execute the method
        assertThrows(IllegalArgumentException.class, () -> featureService.addFeature(request));

        // Verify interactions and assertions
        verify(featureRepository, times(0)).save(any(Feature.class));
        verify(featureVersionRepository, times(0)).save(any(FeatureVersion.class));
        verify(emailNotificationService, times(0)).getNotificationRecipients(2L, null);
        verify(emailNotificationService, times(0)).sendUpdateEmail(eq(request), any(FeatureVersion.class),
                eq(mockPriority), eq(mockStatus), eq(recipients), eq(assignedUser));
    }

    @Test
    public void addFeatureNoAssignedTo() {

        // Mock input request
        FeatureRequest request = new FeatureRequest();
        request.setFeatureStatusID(1);
        request.setPriorityID(2);
        request.setAssignedTo(2L);
        request.setName("New Feature");
        request.setShortDescription("Short Desc");
        request.setURL("http://example.com");

        // Mock return values for dependencies
        User mockUser = new User();
        mockUser.setUserID(1L);
        when(userService.getCurrentUser()).thenReturn(mockUser);

        Feature mockFeature = new Feature(mockUser.getUserID());
        mockFeature.setFeatureID(1L);
        when(featureRepository.save(any(Feature.class))).thenReturn(mockFeature);

        FeatureStatus mockStatus = new FeatureStatus();
        when(featureStatusRepository.findById(1L)).thenReturn(Optional.of(mockStatus));
        when(featureStatusService.getFeatureStatusById(1L)).thenReturn(Optional.of(mockStatus));

        Priority mockPriority = new Priority();
        when(priorityRepository.findById(2L)).thenReturn(Optional.of(mockPriority));
        when(priorityService.getPriorityById(2L)).thenReturn(Optional.of(mockPriority));

        User assignedUser = new User();
        assignedUser.setUserID(2L);
        when(userService.getUserById(2L)).thenReturn(Optional.empty());

        // Mock email notification recipients
        List<String> recipients = List.of("user@example.com");
        when(emailNotificationService.getNotificationRecipients(2L, null)).thenReturn(recipients);

        // Execute the method
        assertThrows(IllegalArgumentException.class, () -> featureService.addFeature(request));

        // Verify interactions and assertions
        verify(featureRepository, times(0)).save(any(Feature.class));
        verify(featureVersionRepository, times(0)).save(any(FeatureVersion.class));
        verify(emailNotificationService, times(0)).getNotificationRecipients(2L, null);
        verify(emailNotificationService, times(0)).sendUpdateEmail(eq(request), any(FeatureVersion.class),
                eq(mockPriority), eq(mockStatus), eq(recipients), eq(assignedUser));
    }

    @Test
    void updateFeatureBestCase() throws BadRequestException {
        // Mock input request
        FeatureRequest request = new FeatureRequest();
        request.setFeatureID(1L);
        request.setFeatureStatusID(2);
        request.setPriorityID(3);
        request.setAssignedTo(4L);
        request.setName("Updated Feature");
        request.setShortDescription("Updated Desc");
        request.setURL("http://updated-example.com");

        // Mock existing feature version
        FeatureVersion latestFeatureVersion = new FeatureVersion();
        latestFeatureVersion.setFeatureVersionID(10L);
        latestFeatureVersion.setFeatureID(1L);
        latestFeatureVersion.setAssignedTo(3L); // Previous assignee

        when(featureVersionService.getLatestFeatureVersionByFeatureId(1L)).thenReturn(Optional.of(latestFeatureVersion));

        // Mock dependencies
        User mockUser = new User();
        mockUser.setUserID(1L);
        when(userService.getCurrentUser()).thenReturn(mockUser);

        Priority mockPriority = new Priority();
        when(priorityService.getPriorityById(3)).thenReturn(Optional.of(mockPriority));

        FeatureStatus mockStatus = new FeatureStatus();
        when(featureStatusService.getFeatureStatusById(2)).thenReturn(Optional.of(mockStatus));

        // Mock new feature version creation
        FeatureVersion newFeatureVersion = new FeatureVersion();
        newFeatureVersion.setFeatureVersionID(11L);
        newFeatureVersion.setFeatureID(1L);
        newFeatureVersion.setAssignedTo(4L); // New assignee

        when(featureVersionRepository.save(any(FeatureVersion.class))).thenReturn(newFeatureVersion);


        // Mock email notification recipients
        List<String> recipients = List.of("user@example.com");
        when(emailNotificationService.getNotificationRecipients(4L, 3L)).thenReturn(recipients);

        // Mock user retrieval
        User assignedUser = new User();
        assignedUser.setUserID(4L);
        when(userService.getUserById(4L)).thenReturn(Optional.of(assignedUser));

        // Mock save operation
        when(featureVersionRepository.save(newFeatureVersion)).thenReturn(newFeatureVersion);

        // Execute the method
        FeatureVersion result = featureService.updateFeature(request);

        // Assertions
        assertNotNull(result);
        assertEquals(11L, result.getFeatureVersionID());

        // Verify interactions
        verify(featureService, times(1)).getLatestFeatureVersion(1L);
        verify(featureService, times(1)).getFeaturePriority(3);
        verify(featureService, times(1)).getFeatureStatus(2);
        verify(featureService, times(1)).createNewFeatureVersion(request, latestFeatureVersion);
        verify(emailNotificationService, times(1)).getNotificationRecipients(4L, 3L);
        verify(emailNotificationService, times(1)).sendUpdateEmail(eq(request), eq(newFeatureVersion), eq(mockPriority), eq(mockStatus), eq(recipients), eq(assignedUser));
        verify(featureVersionRepository, times(1)).save(newFeatureVersion);
    }

    @Test
    void updateFeatureNoCurrentUser() throws BadRequestException {
        // Mock input request
        FeatureRequest request = new FeatureRequest();
        request.setFeatureID(1L);
        request.setFeatureStatusID(2);
        request.setPriorityID(3);
        request.setAssignedTo(4L);
        request.setName("Updated Feature");
        request.setShortDescription("Updated Desc");
        request.setURL("http://updated-example.com");

        // Mock existing feature version
        FeatureVersion latestFeatureVersion = new FeatureVersion();
        latestFeatureVersion.setFeatureVersionID(10L);
        latestFeatureVersion.setFeatureID(1L);
        latestFeatureVersion.setAssignedTo(3L); // Previous assignee

        when(featureVersionService.getLatestFeatureVersionByFeatureId(1L)).thenReturn(Optional.of(latestFeatureVersion));

        // Mock dependencies
        User mockUser = new User();
        mockUser.setUserID(1L);
        when(userService.getCurrentUser()).thenThrow(IllegalStateException.class);

        Priority mockPriority = new Priority();
        when(priorityService.getPriorityById(3)).thenReturn(Optional.of(mockPriority));

        FeatureStatus mockStatus = new FeatureStatus();
        when(featureStatusService.getFeatureStatusById(2)).thenReturn(Optional.of(mockStatus));

        // Mock new feature version creation
        FeatureVersion newFeatureVersion = new FeatureVersion();
        newFeatureVersion.setFeatureVersionID(11L);
        newFeatureVersion.setFeatureID(1L);
        newFeatureVersion.setAssignedTo(4L); // New assignee

        when(featureVersionRepository.save(any(FeatureVersion.class))).thenReturn(newFeatureVersion);


        // Mock email notification recipients
        List<String> recipients = List.of("user@example.com");
        when(emailNotificationService.getNotificationRecipients(4L, 3L)).thenReturn(recipients);

        // Mock user retrieval
        User assignedUser = new User();
        assignedUser.setUserID(4L);
        when(userService.getUserById(4L)).thenReturn(Optional.of(assignedUser));

        // Mock save operation
        when(featureVersionRepository.save(newFeatureVersion)).thenReturn(newFeatureVersion);

        // Execute the method
        assertThrows(IllegalStateException.class, ()->featureService.updateFeature(request));


        // Verify interactions
        verify(featureService, times(0)).createNewFeatureVersion(request, latestFeatureVersion);
        verify(emailNotificationService, times(0)).sendUpdateEmail(eq(request), eq(newFeatureVersion), eq(mockPriority), eq(mockStatus), eq(recipients), eq(assignedUser));
        verify(featureVersionRepository, times(0)).save(newFeatureVersion);
    }

    @Test
    void updateFeatureNoPriority() throws BadRequestException {
        // Mock input request
        FeatureRequest request = new FeatureRequest();
        request.setFeatureID(1L);
        request.setFeatureStatusID(2);
        request.setPriorityID(3);
        request.setAssignedTo(4L);
        request.setName("Updated Feature");
        request.setShortDescription("Updated Desc");
        request.setURL("http://updated-example.com");

        // Mock existing feature version
        FeatureVersion latestFeatureVersion = new FeatureVersion();
        latestFeatureVersion.setFeatureVersionID(10L);
        latestFeatureVersion.setFeatureID(1L);
        latestFeatureVersion.setAssignedTo(3L); // Previous assignee

        when(featureVersionService.getLatestFeatureVersionByFeatureId(1L)).thenReturn(Optional.of(latestFeatureVersion));

        // Mock dependencies
        User mockUser = new User();
        mockUser.setUserID(1L);
        when(userService.getCurrentUser()).thenReturn(mockUser);

        Priority mockPriority = new Priority();
        when(priorityService.getPriorityById(3)).thenReturn(Optional.empty());

        FeatureStatus mockStatus = new FeatureStatus();
        when(featureStatusService.getFeatureStatusById(2)).thenReturn(Optional.of(mockStatus));

        // Mock new feature version creation
        FeatureVersion newFeatureVersion = new FeatureVersion();
        newFeatureVersion.setFeatureVersionID(11L);
        newFeatureVersion.setFeatureID(1L);
        newFeatureVersion.setAssignedTo(4L); // New assignee

        when(featureVersionRepository.save(any(FeatureVersion.class))).thenReturn(newFeatureVersion);


        // Mock email notification recipients
        List<String> recipients = List.of("user@example.com");
        when(emailNotificationService.getNotificationRecipients(4L, 3L)).thenReturn(recipients);

        // Mock user retrieval
        User assignedUser = new User();
        assignedUser.setUserID(4L);
        when(userService.getUserById(4L)).thenReturn(Optional.of(assignedUser));

        // Mock save operation
        when(featureVersionRepository.save(newFeatureVersion)).thenReturn(newFeatureVersion);

        // Execute the method
        assertThrows(IllegalArgumentException.class, ()->featureService.updateFeature(request));


        // Verify interactions
        verify(featureService, times(0)).createNewFeatureVersion(request, latestFeatureVersion);
        verify(emailNotificationService, times(0)).sendUpdateEmail(eq(request), eq(newFeatureVersion), eq(mockPriority), eq(mockStatus), eq(recipients), eq(assignedUser));
        verify(featureVersionRepository, times(0)).save(newFeatureVersion);
    }

    @Test
    void updateFeatureNoFeatureStatus() throws BadRequestException {
        // Mock input request
        FeatureRequest request = new FeatureRequest();
        request.setFeatureID(1L);
        request.setFeatureStatusID(2);
        request.setPriorityID(3);
        request.setAssignedTo(4L);
        request.setName("Updated Feature");
        request.setShortDescription("Updated Desc");
        request.setURL("http://updated-example.com");

        // Mock existing feature version
        FeatureVersion latestFeatureVersion = new FeatureVersion();
        latestFeatureVersion.setFeatureVersionID(10L);
        latestFeatureVersion.setFeatureID(1L);
        latestFeatureVersion.setAssignedTo(3L); // Previous assignee

        when(featureVersionService.getLatestFeatureVersionByFeatureId(1L)).thenReturn(Optional.of(latestFeatureVersion));

        // Mock dependencies
        User mockUser = new User();
        mockUser.setUserID(1L);
        when(userService.getCurrentUser()).thenReturn(mockUser);

        Priority mockPriority = new Priority();
        when(priorityService.getPriorityById(3)).thenReturn(Optional.of(mockPriority));

        FeatureStatus mockStatus = new FeatureStatus();
        when(featureStatusService.getFeatureStatusById(2)).thenReturn(Optional.empty());

        // Mock new feature version creation
        FeatureVersion newFeatureVersion = new FeatureVersion();
        newFeatureVersion.setFeatureVersionID(11L);
        newFeatureVersion.setFeatureID(1L);
        newFeatureVersion.setAssignedTo(4L); // New assignee

        when(featureVersionRepository.save(any(FeatureVersion.class))).thenReturn(newFeatureVersion);


        // Mock email notification recipients
        List<String> recipients = List.of("user@example.com");
        when(emailNotificationService.getNotificationRecipients(4L, 3L)).thenReturn(recipients);

        // Mock user retrieval
        User assignedUser = new User();
        assignedUser.setUserID(4L);
        when(userService.getUserById(4L)).thenReturn(Optional.of(assignedUser));

        // Mock save operation
        when(featureVersionRepository.save(newFeatureVersion)).thenReturn(newFeatureVersion);

        // Execute the method
        assertThrows(IllegalArgumentException.class, ()->featureService.updateFeature(request));


        // Verify interactions
        verify(featureService, times(0)).createNewFeatureVersion(request, latestFeatureVersion);
        verify(emailNotificationService, times(0)).sendUpdateEmail(eq(request), eq(newFeatureVersion), eq(mockPriority), eq(mockStatus), eq(recipients), eq(assignedUser));
        verify(featureVersionRepository, times(0)).save(newFeatureVersion);
    }

}