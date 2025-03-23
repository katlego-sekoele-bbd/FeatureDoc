package com.featuredoc.services;

import com.featuredoc.dto.FeatureRequest;
import com.featuredoc.models.*;
import com.featuredoc.repository.FeatureRepository;
import com.featuredoc.repository.FeatureVersionRepository;
import jakarta.validation.ConstraintViolationException;
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
        private FeatureViewService Service;

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
                FeatureStatus featureStatus = getFeatureStatus(request.getFeatureStatusID());
                Priority priority = getFeaturePriority(request.getPriorityID());
                User assignedTo = validateAssignedTo(request.getAssignedTo());
                FeatureVersion featureVersion = new FeatureVersion(
                                currentlyLoggedInUser.getUserID(),
                                feature.getFeatureID(),
                                request.getFeatureStatusID(),
                                request.getPriorityID(),
                                assignedTo != null ? assignedTo.getUserID() : null,
                                request.getName(),
                                request.getShortDescription(),
                                request.getURL());
                featureVersionRepository.save(featureVersion);
                List<String> recipients = emailNotificationService
                                .getNotificationRecipients(featureVersion.getAssignedTo(), null);
                emailNotificationService.sendUpdateEmail(request, featureVersion, priority, featureStatus, recipients,
                        request.getAssignedTo());
                return feature;
        }

        @Transactional
        public FeatureVersion updateFeature(FeatureRequest request) throws BadRequestException {
                FeatureVersion latestFeatureVersion = getLatestFeatureVersion(request.getFeatureID());
                Priority priority = getFeaturePriority(request.getPriorityID());
                FeatureStatus featureStatus = getFeatureStatus(request.getFeatureStatusID());
                FeatureVersion newVersion = createNewFeatureVersion(request, latestFeatureVersion);
                List<String> recipients = emailNotificationService.getNotificationRecipients(newVersion.getAssignedTo(),latestFeatureVersion.getAssignedTo());
                emailNotificationService.sendUpdateEmail(request, newVersion, priority, featureStatus, recipients, request.getAssignedTo());
                return featureVersionRepository.save(newVersion);
        }

        FeatureVersion getLatestFeatureVersion(Long featureID) throws BadRequestException {
                return featureVersionService.getLatestFeatureVersionByFeatureId(featureID)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "No previous version found for Feature ID: " + featureID));
        }

        Priority getFeaturePriority(Integer priorityID) {
                if (priorityID == null) {
                        return null;
                } else {
                        return priorityService.getPriorityById(priorityID)
                                        .orElseThrow(() -> new IllegalArgumentException(
                                                        "Priority not found for ID: " + priorityID));
                }
        }

        User validateAssignedTo(Long userID) {

                if (userID == null) {
                        return null;
                } else {
                        return userService.getUserById(userID)
                                        .orElseThrow(() -> new IllegalArgumentException(
                                                        "No user found for given assignedTo ID "
                                                                        + userID));
                }
        }

        FeatureStatus getFeatureStatus(Integer featureStatusID) {
                if (featureStatusID == null)
                        return null;
                return featureStatusService.getFeatureStatusById(featureStatusID)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "FeatureStatus not found for ID: " + featureStatusID));
        }

        FeatureVersion createNewFeatureVersion(FeatureRequest request, FeatureVersion latestFeatureVersion) {
                User currentlyLoggedInUser = userService.getCurrentUser();
                FeatureVersion newVersion = new FeatureVersion(
                                currentlyLoggedInUser.getUserID(),
                                request.getFeatureID(),
                                request.getFeatureStatusID() != null ? request.getFeatureStatusID()
                                                : latestFeatureVersion.getFeatureStatusID(),
                                request.getPriorityID() != null ? request.getPriorityID()
                                                : latestFeatureVersion.getPriorityID(),
                                request.getAssignedTo() != null ? request.getAssignedTo()
                                                : latestFeatureVersion.getAssignedTo(),
                                request.getName() != null ? request.getName() : latestFeatureVersion.getName(),
                                request.getShortDescription() != null ? request.getShortDescription()
                                                : latestFeatureVersion.getShortDescription(),
                                request.getURL() != null ? request.getURL() : latestFeatureVersion.getURL());
                return featureVersionRepository.save(newVersion);
        }
}

