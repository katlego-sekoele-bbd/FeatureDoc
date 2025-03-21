package com.featuredoc.services;

import com.featuredoc.dto.FeatureRequest;
import com.featuredoc.models.*;
import com.featuredoc.repository.FeatureRepository;
import com.featuredoc.repository.FeatureVersionRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FeatureService {

        @Autowired
        private FeatureVersionRepository featureVersionRepository;

        @Autowired
        private UserService userService;

        @Autowired
        FeatureStatusService featureStatusService;

        @Autowired
        PriorityService priorityService;

        @Autowired
        private FeatureRepository featureRepository;

        @Autowired
        private FeatureVersionService featureVersionService;

        @Autowired
        private EmailNotificationService emailNotificationService;

    @Transactional
    public Feature addFeature(FeatureRequest request) {
        User currentlyLoggedInUser = userService.getCurrentUser();
        Feature feature = new Feature(currentlyLoggedInUser.getUserID());
        feature = featureRepository.save(feature);
            Priority priority = getFeaturePriority(request.getPriorityID());
            FeatureStatus featureStatus = getFeatureStatus(request.getFeatureStatusID());
        FeatureVersion featureVersion = new FeatureVersion(
                currentlyLoggedInUser.getUserID(),
                feature.getFeatureID(),
                request.getFeatureStatusID(),
                request.getPriorityID(),
                request.getAssignedTo(),
                request.getName(),
                request.getShortDescription(),
                request.getURL());
                featureVersionRepository.save(featureVersion);
            List<String> recipients = emailNotificationService.getNotificationRecipients(featureVersion .getAssignedTo(),null);
            User user = userService.getUserById(request.getAssignedTo()).get();
            emailNotificationService.sendUpdateEmail(request, featureVersion, priority, featureStatus, recipients, user);
                return feature;
        }

        @Transactional
        public FeatureVersion updateFeature(FeatureRequest request) throws BadRequestException {
                FeatureVersion latestFeatureVersion = getLatestFeatureVersion(request.getFeatureID());
                Priority priority = getFeaturePriority(request.getPriorityID());
                FeatureStatus featureStatus = getFeatureStatus(request.getFeatureStatusID());
                FeatureVersion newVersion = createNewFeatureVersion(request, latestFeatureVersion);
                List<String> recipients = emailNotificationService.getNotificationRecipients(newVersion.getAssignedTo(),latestFeatureVersion.getAssignedTo());
                User user = userService.getUserById(request.getAssignedTo()).get();
                emailNotificationService.sendUpdateEmail(request, newVersion, priority, featureStatus, recipients, user);
                return featureVersionRepository.save(newVersion);
        }

        private void validateRequest(FeatureRequest request) {
                if (request == null) {
                        throw new IllegalArgumentException("FeatureRequest cannot be null");
                } else {
                        // body is not empty
                }
        }

        private FeatureVersion getLatestFeatureVersion(Long featureID) throws BadRequestException {
                return featureVersionService.getLatestFeatureVersionByFeatureId(featureID)
                                .orElseThrow(() -> new BadRequestException(
                                                "No previous version found for Feature ID: " + featureID));
        }

        private Priority getFeaturePriority(Integer priorityID) {
                if (priorityID == null) {
                        return null;

                } else {
                        return priorityService.getPriorityById(priorityID).orElseThrow(
                                        () -> new RuntimeException("Priority not found for ID: " + priorityID));

                }

        }

        private FeatureStatus getFeatureStatus(Integer featureStatusID) {
                if (featureStatusID == null)
                        return null;
                return featureStatusService.getFeatureStatusById(featureStatusID)
                                .orElseThrow(() -> new RuntimeException(
                                                "FeatureStatus not found for ID: " + featureStatusID));
        }

        private FeatureVersion createNewFeatureVersion(FeatureRequest request, FeatureVersion latestFeatureVersion) {
                User currentlyLoggedInUser = userService.getCurrentUser();
        FeatureVersion newVersion = new FeatureVersion(
                currentlyLoggedInUser.getUserID(),
                request.getFeatureID(),
                request.getFeatureStatusID() != null ? request.getFeatureStatusID()
                        : latestFeatureVersion.getFeatureStatusID(),
                request.getPriorityID() != null ? request.getPriorityID() : latestFeatureVersion.getPriorityID(),
                request.getAssignedTo() != null ? request.getAssignedTo() : latestFeatureVersion.getAssignedTo(),
                request.getName() != null ? request.getName() : latestFeatureVersion.getName(),
                request.getShortDescription() != null ? request.getShortDescription()
                        : latestFeatureVersion.getShortDescription(),
                request.getURL() != null ? request.getURL() : latestFeatureVersion.getURL());
        return featureVersionRepository.save(newVersion);
    }
}

