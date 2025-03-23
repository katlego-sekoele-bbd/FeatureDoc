package com.featuredoc.services;

import com.featuredoc.dto.FeatureRequest;
import com.featuredoc.models.User;
import com.featuredoc.models.FeatureVersion;
import com.featuredoc.models.Priority;
import com.featuredoc.models.FeatureStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
public class EmailNotificationService {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;
    public List<String> getNotificationRecipients(Long newAssignedUserID, Long previouslyAssignedUserID) {
        List<String> recipients = new ArrayList<>();

        // Case 1: AssignedTo was changed to null (notify previously assigned user)
        if (newAssignedUserID == null && previouslyAssignedUserID != null) {
            addUserEmailToRecipients(previouslyAssignedUserID, recipients);
        }
        // Case 2: AssignedTo was changed from one user to another (notify both)
        else if (newAssignedUserID != null && previouslyAssignedUserID != null) {
            addUserEmailToRecipients(previouslyAssignedUserID, recipients);
            addUserEmailToRecipients(newAssignedUserID, recipients);
        }
        // Case 3: AssignedTo was changed from null to a user (notify new assigned user)
        else if (newAssignedUserID != null && previouslyAssignedUserID == null) {
            addUserEmailToRecipients(newAssignedUserID, recipients);
        }

        return recipients;
    }

    private void addUserEmailToRecipients(Long userId, List<String> recipients) {
        Optional<User> user = userService.getUserById(userId);
        user.ifPresent(u -> recipients.add(u.getEmail()));
    }

    public void sendUpdateEmail(FeatureRequest request, FeatureVersion newVersion, Priority priority, FeatureStatus featureStatus, List<String> recipients, Long assignedUser) {
        System.out.println("Email function executed");
        if (recipients.isEmpty()) {
            return; // No recipients to notify
        }
        System.out.println("recipients confirmed");


        StringBuilder emailContent = new StringBuilder();
        emailContent.append("Hello,\n\n");
        emailContent.append("The feature '").append(newVersion.getName()).append("' has been updated.\n\n");

        // Include only the fields that were provided in the request
        if (request.getFeatureStatusID() != null) {
            String statusDescription = featureStatus != null ? featureStatus.getDescription() : "N/A";
            emailContent.append("Status: ").append(statusDescription).append("\n");
        }
        if (request.getPriorityID() != null) {
            String priorityDescription = priority != null ? priority.getDescription() : "N/A";
            emailContent.append("Priority: ").append(priorityDescription).append("\n");
        }
        if (assignedUser != null) {
            emailContent.append("Assigned To: ").append(userService.getUserById(assignedUser).get().getName()).append("\n");
        }

        if (request.getName() != null) {
            emailContent.append("Name: ").append(newVersion.getName()).append("\n");
        }
        if (request.getShortDescription() != null) {
            emailContent.append("Short Description: ").append(newVersion.getShortDescription()).append("\n");
        }
        if (request.getURL() != null) {
            emailContent.append("URL: ").append(newVersion.getURL()).append("\n");
        }

        emailContent.append("\nRegards,\nFeature Management System");

        String subject = "Feature Updated: " + newVersion.getName();

        // Send email to all recipients at once
        emailService.sendSimpleMessage(recipients, subject, emailContent.toString());
    }
}