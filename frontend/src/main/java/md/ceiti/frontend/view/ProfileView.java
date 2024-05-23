package md.ceiti.frontend.view;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import md.ceiti.frontend.constant.ConstraintViolationMessage;
import md.ceiti.frontend.constant.I18n;
import md.ceiti.frontend.constant.fields.ProfileChangePasswordRequestFields;
import md.ceiti.frontend.constant.fields.ProfileUpdateRequestFields;
import md.ceiti.frontend.dto.request.ProfileChangePasswordRequest;
import md.ceiti.frontend.dto.request.ProfileUpdateRequest;
import md.ceiti.frontend.dto.response.Profile;
import md.ceiti.frontend.exception.BadRequestException;
import md.ceiti.frontend.exception.ValidationException;
import md.ceiti.frontend.mapper.GenericMapper;
import md.ceiti.frontend.service.ProfileService;
import md.ceiti.frontend.util.ComponentUtils;
import md.ceiti.frontend.util.ErrorHandler;
import md.ceiti.frontend.util.NavigationUtils;
import org.apache.commons.lang3.tuple.Pair;

@Route(value = "profile")
@PageTitle(value = "DM | Profile")
public class ProfileView extends NavigationUtilsView {

    private final ProfileService profileService;
    private final GenericMapper mapper;
    private final BeanValidationBinder<ProfileUpdateRequest> updateProfileBinder = new BeanValidationBinder<>(ProfileUpdateRequest.class);
    private final BeanValidationBinder<ProfileChangePasswordRequest> changePasswordBinder =
            new BeanValidationBinder<>(ProfileChangePasswordRequest.class);
    private final ProfileChangePasswordRequest profileChangePasswordRequest = new ProfileChangePasswordRequest();
    private Profile profile;
    private ProfileUpdateRequest profileUpdateRequest;
    private ProfileUpdateRequest backup;

    public ProfileView(ProfileService profileService, GenericMapper mapper) {
        this.profileService = profileService;
        this.mapper = mapper;
        try {
            profile = profileService.getProfile();
            profileUpdateRequest = mapper.toUpdateRequest(profile);
        } catch (BadRequestException e) {
            ErrorHandler.handle(e);
            return;
        }

        buildView();
    }

    private void buildView() {
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.AROUND);

        Avatar profileImageAvatar = ComponentUtils.getAvatar(profile, true);
        profileImageAvatar.getElement().addEventListener("click", event -> {
            Notification.show("Yeesss");
        });
        FormLayout profileForm = getProfileForm();
        disableProfileForm(profileForm);

        Div container = new Div();
        container.setClassName("profile-container");
        container.add(
                profileImageAvatar,
                profileForm,
                getChangePasswordLayout(),
                getButtonLayout(profileForm)
        );

        add(container);
    }

    private Dialog getChangeImageDialog() {
        Dialog dialog = new Dialog();
        dialog.setModal(true);
        return dialog;
    }

    private FormLayout getProfileForm() {
        TextField username = new TextField(ProfileUpdateRequestFields.USERNAME.getLabel(), ProfileUpdateRequestFields.USERNAME.getExample());
        username.setId(ProfileUpdateRequestFields.USERNAME.getId());

        TextField firstName = new TextField(ProfileUpdateRequestFields.FIRST_NAME.getLabel(), ProfileUpdateRequestFields.FIRST_NAME.getExample());
        firstName.setId(ProfileUpdateRequestFields.FIRST_NAME.getId());

        TextField lastName = new TextField(ProfileUpdateRequestFields.LAST_NAME.getLabel(), ProfileUpdateRequestFields.LAST_NAME.getExample());
        lastName.setId(ProfileUpdateRequestFields.LAST_NAME.getId());

        TextField phoneNumber = new TextField(ProfileUpdateRequestFields.PHONE_NUMBER.getLabel(), ProfileUpdateRequestFields.PHONE_NUMBER.getExample());
        phoneNumber.setId(ProfileUpdateRequestFields.PHONE_NUMBER.getId());

        DatePicker.DatePickerI18n datePickerI18n = new DatePicker.DatePickerI18n();
        datePickerI18n.setDateFormat(I18n.DATE_FORMAT);
        DatePicker birthDate = new DatePicker(ProfileUpdateRequestFields.BIRTH_DATE.getLabel());
        birthDate.setId(ProfileUpdateRequestFields.BIRTH_DATE.getId());
        birthDate.setI18n(datePickerI18n);
        birthDate.setPlaceholder(ProfileUpdateRequestFields.BIRTH_DATE.getExample());

        EmailField email = new EmailField(ProfileUpdateRequestFields.EMAIL.getLabel());
        email.setId(ProfileUpdateRequestFields.EMAIL.getId());
        email.setPlaceholder(ProfileUpdateRequestFields.EMAIL.getExample());

        updateProfileBinder.bind(username, ProfileUpdateRequestFields.USERNAME.getId());
        updateProfileBinder.bind(firstName, ProfileUpdateRequestFields.FIRST_NAME.getId());
        updateProfileBinder.bind(lastName, ProfileUpdateRequestFields.LAST_NAME.getId());
        updateProfileBinder.bind(phoneNumber, ProfileUpdateRequestFields.PHONE_NUMBER.getId());
        updateProfileBinder.bind(email, ProfileUpdateRequestFields.EMAIL.getId());
        updateProfileBinder.bind(birthDate, ProfileUpdateRequestFields.BIRTH_DATE.getId());
        updateProfileBinder.setBean(profileUpdateRequest);

        FormLayout formLayout = new FormLayout();
        formLayout.add(username, firstName, lastName, birthDate, email, phoneNumber);
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1)
        );

        return formLayout;
    }

    private HorizontalLayout getButtonLayout(FormLayout form) {
        Button back = new Button(I18n.BACK);
        back.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        Button edit = new Button(I18n.EDIT);
        edit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancel = new Button(I18n.CANCEL);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancel.setVisible(false);

        Button save = new Button(I18n.SAVE);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.setVisible(false);

        back.addClickListener(event -> navigateToPreviousPage());
        edit.addClickListener(event -> {
            back.setVisible(false);
            edit.setVisible(false);
            cancel.setVisible(true);
            save.setVisible(true);
            enableProfileForm(form);
            backup = profileUpdateRequest;
        });
        cancel.addClickListener(event -> {
            back.setVisible(true);
            edit.setVisible(true);
            cancel.setVisible(false);
            save.setVisible(false);
            disableProfileForm(form);
            profileUpdateRequest = backup;
            updateProfileBinder.setBean(profileUpdateRequest);
        });
        save.addClickListener(event -> {
            if (!updateProfileBinder.isValid()) {
                updateProfileBinder.validate();
                return;
            }

            try {
                Profile updatedProfile = profileService.updateProfile(profileUpdateRequest);
                if (!profile.getUsername().equals(updatedProfile.getUsername())) {
                    NavigationUtils.navigateTo(LoginView.class);
                } else {
                    profile = updatedProfile;
                }
            } catch (BadRequestException | ValidationException e) {
                if (e instanceof ValidationException validationException) {
                    ErrorHandler.setErrors(form, validationException);
                } else {
                    ErrorHandler.handle((BadRequestException) e);
                }
            }
            backup = mapper.toUpdateRequest(profile);
            cancel.click();
        });

        HorizontalLayout container = new HorizontalLayout();
        container.add(back, edit, cancel, save);
        container.setSizeFull();
        container.setJustifyContentMode(JustifyContentMode.BETWEEN);

        return container;
    }

    private HorizontalLayout getChangePasswordLayout() {
        Button button = new Button(I18n.CHANGE_PASSWORD);
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        button.addClickListener(event -> getChangePasswordDialog(getChangePasswordForm()).open());

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSizeFull();
        horizontalLayout.add(button);

        return horizontalLayout;
    }

    private Dialog getChangePasswordDialog(FormLayout form) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(I18n.CHANGE_PASSWORD);
        dialog.add(form);
        dialog.add(getChangePasswordDialogButtonLayout(dialog, form));
        dialog.setCloseOnOutsideClick(false);

        return dialog;
    }

    private FormLayout getChangePasswordForm() {
        PasswordField currentPassword = new PasswordField(ProfileChangePasswordRequestFields.CURRENT_PASSWORD.getLabel());
        PasswordField newPassword = new PasswordField(ProfileChangePasswordRequestFields.NEW_PASSWORD.getLabel());
        PasswordField confirmPassword = new PasswordField(ProfileChangePasswordRequestFields.CONFIRM_PASSWORD.getLabel());

        currentPassword.setId(ProfileChangePasswordRequestFields.CURRENT_PASSWORD.getId());
        newPassword.setId(ProfileChangePasswordRequestFields.NEW_PASSWORD.getId());
        confirmPassword.setId(ProfileChangePasswordRequestFields.CONFIRM_PASSWORD.getId());

        Binder.Binding<ProfileChangePasswordRequest, String> currentPasswordBinding =
                changePasswordBinder.bind(currentPassword, ProfileChangePasswordRequestFields.CURRENT_PASSWORD.getId());
        Binder.Binding<ProfileChangePasswordRequest, String> newPasswordBinding =
                changePasswordBinder.forField(newPassword)
                        .asRequired(ProfileChangePasswordRequestFields.NEW_PASSWORD.getLabel() + ConstraintViolationMessage.REQUIRED)
                        .withValidator(value -> value.equals(confirmPassword.getValue()) || confirmPassword.getValue().isBlank(),
                                ConstraintViolationMessage.PASSWORDS_NOT_MATCH)
                        .bind(ProfileChangePasswordRequestFields.NEW_PASSWORD.getId());
        Binder.Binding<ProfileChangePasswordRequest, String> confirmPasswordBinding =
                changePasswordBinder.forField(confirmPassword)
                        .asRequired(ProfileChangePasswordRequestFields.CONFIRM_PASSWORD.getLabel() + ConstraintViolationMessage.REQUIRED)
                        .withValidator(value -> value.equals(newPassword.getValue()) || newPassword.getValue().isBlank(),
                                ConstraintViolationMessage.PASSWORDS_NOT_MATCH)
                        .bind(ProfileChangePasswordRequestFields.CONFIRM_PASSWORD.getId());
        changePasswordBinder.setBean(profileChangePasswordRequest);

        newPassword.addValueChangeListener(event -> {
            newPasswordBinding.validate();
            confirmPasswordBinding.validate();
        });
        confirmPassword.addValueChangeListener(event -> {
            newPasswordBinding.validate();
            confirmPasswordBinding.validate();
        });
        currentPassword.addValueChangeListener(event -> currentPasswordBinding.validate());

        FormLayout formLayout = new FormLayout();
        formLayout.addClassName("change-password-form-layout");
        formLayout.add(currentPassword, newPassword, confirmPassword);

        return formLayout;
    }

    private HorizontalLayout getChangePasswordDialogButtonLayout(Dialog dialog, FormLayout form) {
        Pair<HorizontalLayout, Pair<Button, Button>> dialogButtonLayout = ComponentUtils.getGenericDialogButtonLayout(dialog);
        dialogButtonLayout.getRight().getRight().addClickListener(event -> {
            if (!changePasswordBinder.isValid()) {
                changePasswordBinder.validate();
                return;
            }

            try {
                profileService.changePassword(profileChangePasswordRequest);
                NavigationUtils.navigateTo(LoginView.class);
                dialog.close();
            } catch (BadRequestException | ValidationException e) {
                if (e instanceof ValidationException validationException) {
                    ErrorHandler.setErrors(form, validationException);
                } else {
                    ErrorHandler.handle((BadRequestException) e);
                    dialog.close();
                }
            }
        });

        return dialogButtonLayout.getLeft();
    }

    private void disableProfileForm(FormLayout profileForm) {
        profileForm.getChildren().forEach(component -> {
            if (component instanceof HasValue) {
                ((HasValue<?, ?>) component).setReadOnly(true);
            }
        });
    }

    private void enableProfileForm(FormLayout profileForm) {
        profileForm.getChildren().forEach(component -> {
            if (component instanceof HasValue) {
                ((HasValue<?, ?>) component).setReadOnly(false);
            }
        });
    }
}
