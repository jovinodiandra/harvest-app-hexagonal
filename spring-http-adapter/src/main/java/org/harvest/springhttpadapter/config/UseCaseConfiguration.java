package org.harvest.springhttpadapter.config;

import org.harvest.application.dto.command.*;
import org.harvest.application.dto.query.*;
import org.harvest.application.dto.query.VIewDiseasesRecordQuery;
import org.harvest.application.dto.result.*;
import org.harvest.application.port.inbound.UseCase;
import org.harvest.application.port.outbound.*;
import org.harvest.application.port.outbound.security.SessionManager;
import org.harvest.application.port.service.ReminderSchedulerService;
import org.harvest.application.usecase.*;
import org.harvest.application.usecase.contact.CreateContactUseCase;
import org.harvest.application.usecase.contact.DeleteContactUseCase;
import org.harvest.application.usecase.contact.UpdateContactUseCase;
import org.harvest.application.usecase.contact.ViewContactUseCase;
import org.harvest.application.usecase.death.CreateDeathRecordUseCase;
import org.harvest.application.usecase.death.DeleteDeathRecordUseCase;
import org.harvest.application.usecase.death.UpdateDeathRecordUseCase;
import org.harvest.application.usecase.death.ViewDeathRecordUseCase;
import org.harvest.application.usecase.diseases.CreateDiseasesRecordUseCase;
import org.harvest.application.usecase.feedprice.CreateFeedPriceUseCase;
import org.harvest.application.usecase.feedreminder.CreateFeedReminderUseCase;
import org.harvest.application.usecase.feedschedule.CreateFeedScheduleUseCase;
import org.harvest.application.usecase.finance.CreateFinanceRecordUseCase;
import org.harvest.application.usecase.growth.CreateGrowthRecordUseCase;
import org.harvest.application.usecase.harvest.CreateHarvestRecordUseCase;
import org.harvest.application.usecase.harvest.CreateHarvestReminderUseCase;
import org.harvest.application.usecase.pond.CreatePondsUseCase;
import org.harvest.application.usecase.seed.CreateSeedUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfiguration {
    @Bean("registerUseCase")
    public UseCase<RegisterCommand, RegisterResult> registerUseCase(UserRepository userRepository, OrganizationRepository organizationRepository, PasswordSecurity passwordSecurity){
        return new RegisterUseCase(userRepository, organizationRepository, passwordSecurity);
    }

    @Bean("loginUseCase")
    public UseCase<LoginCommand, LoginResult> loginUseCase(UserRepository userRepository, OrganizationRepository organizationRepository, SessionManager sessionManager, PasswordSecurity passwordSecurity){
        return new LoginUseCase(userRepository, organizationRepository, sessionManager, passwordSecurity);
    }

    @Bean("viewUserUseCase")
    public UseCase<ViewUserQuery, ViewUserResult> viewUserUseCase(UserRepository userRepository){
        return new ViewUserUseCase(userRepository);
    }

    @Bean("createUserUseCase")
    public UseCase<CreateUserCommand, CreateUserResult> createUserUseCase(UserRepository userRepository, PasswordSecurity passwordSecurity){
        return new CreateUserUseCase(userRepository, passwordSecurity);
    }

    @Bean("updateUserUseCase")
    public UseCase<UpdateUserCommand, UpdateUserResult> updateUserUseCase(UserRepository userRepository){
        return new UpdateUserUseCase(userRepository);
    }

    @Bean("deleteUserUseCase")
    public UseCase<DeleteUserCommand, DefaultResult> deleteUserUseCase(UserRepository userRepository){
        return new DeleteUserUseCase(userRepository);
    }

    @Bean("createPondsUseCase")
    public UseCase<CreatePondsCommand, CreatePondsResult> createPondsUseCase(PondsRepository pondsRepository){
        return new CreatePondsUseCase(pondsRepository);
    }

    @Bean("viewPondsUseCase")
    public UseCase<ViewPondsQuery, ViewPondsResult> viewPondsUseCase(PondsRepository pondsRepository){
        return new ViewPondsUseCase(pondsRepository);
    }
    @Bean("updatePondsUseCase")
    public UseCase<UpdatePondsCommand, UpdatePondsResult> updatePondsUseCase(PondsRepository pondsRepository){
        return new UpdatePondsUseCase(pondsRepository);
    }
    @Bean("deletePondsUseCase")
    public UseCase<DeletePondsCommand, DefaultResult> deletePondsUseCase(PondsRepository pondsRepository){
        return new DeletePondsUseCase(pondsRepository);
    }

    @Bean("createSeedUseCase")
    public UseCase<CreateSeedCommand, CreateSeedResult> createSeedUseCase(SeedRepository seedRepository){
        return new CreateSeedUseCase(seedRepository);
    }

    @Bean("viewSeedUseCase")
    public UseCase<ViewSeedQuery, ViewSeedResult> viewSeedUseCase(SeedRepository seedRepository){
        return new VIewSeedUseCase(seedRepository);
    }

    @Bean("updateSeedUseCase")
    public UseCase<UpdateSeedCommand, UpdateSeedResult> updateSeedUseCase(SeedRepository seedRepository, PondsRepository pondsRepository){
        return new UpdateSeedUseCase(seedRepository, pondsRepository);
    }

    @Bean("deleteSeedUseCase")
    public UseCase<DeleteSeedCommand, DefaultResult> deleteSeedUseCase(SeedRepository seedRepository){
        return new DeleteSeedUseCase(seedRepository);
    }

    @Bean("createGrowthRecordUseCase")
    public UseCase<CreateGrowthRecordCommand, CreateGrowthRecordResult> createGrowthRecordUseCase(GrowthRecordRepository growthRecordRepository, PondsRepository pondsRepository) {
        return new CreateGrowthRecordUseCase(growthRecordRepository, pondsRepository);
    }

    @Bean("viewGrowthRecordUseCase")
    public UseCase<ViewGrowthRecordQuery, ViewGrowthRecordResult> viewGrowthRecordUseCase(GrowthRecordRepository growthRecordRepository) {
        return new ViewGrowthRecordUseCase(growthRecordRepository);
    }

    @Bean("updateGrowthRecordUseCase")
    public UseCase<UpdateGrowthRecordCommand, UpdateGrowthRecordResult> updateGrowthRecordUseCase(GrowthRecordRepository growthRecordRepository, PondsRepository pondsRepository) {
        return new UpdateGrowthRecordUseCase(growthRecordRepository, pondsRepository);
    }

    @Bean("deleteGrowthRecordUseCase")
    public UseCase<DeleteGrowthRecordCommand, DefaultResult> deleteGrowthRecordUseCase(GrowthRecordRepository growthRecordRepository) {
        return new DeleteGrowthRecordUseCase(growthRecordRepository);
    }

    @Bean("createDeathRecordUseCase")
    public UseCase<CreateDeathRecordCommand, CreateDeathRecordResult> createDeathRecordUseCase(DeathRecordRepository deathRecordRepository, PondsRepository pondsRepository) {
        return new CreateDeathRecordUseCase(deathRecordRepository, pondsRepository);
    }

    @Bean("viewDeathRecordUseCase")
    public UseCase<ViewDeathRecordQuery, ViewDeathRecordResult> viewDeathRecordUseCase(DeathRecordRepository deathRecordRepository, PondsRepository pondsRepository) {
        return new ViewDeathRecordUseCase(deathRecordRepository, pondsRepository);
    }

    @Bean("updateDeathRecordUseCase")
    public UseCase<UpdateDeathRecordCommand, UpdateDeathRecordResult> updateDeathRecordUseCase(DeathRecordRepository deathRecordRepository, PondsRepository pondsRepository) {
        return new UpdateDeathRecordUseCase(deathRecordRepository, pondsRepository);
    }

    @Bean("deleteDeathRecordUseCase")
    public UseCase<DeleteDeathRecordCommand, DefaultResult> deleteDeathRecordUseCase(DeathRecordRepository deathRecordRepository) {
        return new DeleteDeathRecordUseCase(deathRecordRepository);
    }

    @Bean("createHarvestReminderUseCase")
    public UseCase<CreateHarvestReminderCommand, CreateHarvestReminderResult> createHarvestReminderUseCase(HarvestReminderRepository harvestReminderRepository, PondsRepository pondsRepository, ReminderSchedulerService reminderSchedulerService) {
        return new CreateHarvestReminderUseCase(harvestReminderRepository, pondsRepository, reminderSchedulerService);
    }

    @Bean("viewHarvestReminderUseCase")
    public UseCase<ViewHarvestReminderQuery, ViewHarvestReminderResult> viewHarvestReminderUseCase(HarvestReminderRepository harvestReminderRepository, PondsRepository pondsRepository) {
        return new ViewHarvestReminderUseCase(harvestReminderRepository, pondsRepository);
    }

    @Bean("updateHarvestReminderUseCase")
    public UseCase<UpdateHarvestReminderCommand, UpdateHarvestReminderResult> updateHarvestReminderUseCase(HarvestReminderRepository harvestReminderRepository, PondsRepository pondsRepository, ReminderSchedulerService reminderSchedulerService) {
        return new UpdateHarvestReminderUseCase(harvestReminderRepository, pondsRepository, reminderSchedulerService);
    }

    @Bean("deleteHarvestReminderUseCase")
    public UseCase<DeleteHarvestReminderCommand, DefaultResult> deleteHarvestReminderUseCase(HarvestReminderRepository harvestReminderRepository, ReminderSchedulerService reminderSchedulerService) {
        return new DeleteHarvestReminderUseCase(harvestReminderRepository, reminderSchedulerService);
    }

    @Bean("createDiseasesRecordUseCase")
    public UseCase<CreateDiseasesRecordCommand, CreateDiseasesRecordResult> createDiseasesRecordUseCase(DiseasesRecordRepository diseasesRecordRepository, PondsRepository pondsRepository) {
        return new CreateDiseasesRecordUseCase(diseasesRecordRepository, pondsRepository);
    }

    @Bean("viewDiseasesRecordUseCase")
    public UseCase<VIewDiseasesRecordQuery, ViewDiseasesRecordResult> viewDiseasesRecordUseCase(DiseasesRecordRepository diseasesRecordRepository, PondsRepository pondsRepository) {
        return new ViewDiseasesRecordUseCase(diseasesRecordRepository, pondsRepository);
    }

    @Bean("updateDiseasesRecordUseCase")
    public UseCase<UpdateDiseasesRecordCommand, UpdateDiseasesRecordResult> updateDiseasesRecordUseCase(DiseasesRecordRepository diseasesRecordRepository, PondsRepository pondsRepository) {
        return new UpdateDiseasesRecordUseCase(diseasesRecordRepository, pondsRepository);
    }

    @Bean("deleteDiseasesRecordUseCase")
    public UseCase<DeleteDiseasesRecordCommand, DefaultResult> deleteDiseasesRecordUseCase(DiseasesRecordRepository diseasesRecordRepository) {
        return new DeleteDiseasesRecordUseCase(diseasesRecordRepository);
    }

    @Bean("createHarvestRecordUseCase")
    public UseCase<CreateHarvestRecordCommand, CreateHarvestRecordResult> createHarvestRecordUseCase(HarvestRecordRepository harvestRecordRepository, PondsRepository pondsRepository) {
        return new CreateHarvestRecordUseCase(harvestRecordRepository, pondsRepository);
    }

    @Bean("viewHarvestRecordUseCase")
    public UseCase<ViewHarvestRecordQuery, ViewHarvestRecordResult> viewHarvestRecordUseCase(HarvestRecordRepository harvestRecordRepository) {
        return new ViewHarvestRecordUseCase(harvestRecordRepository);
    }

    @Bean("updateHarvestRecordUseCase")
    public UseCase<UpdateHarvestRecordCommand, UpdateHarvestRecordResult> updateHarvestRecordUseCase(HarvestRecordRepository harvestRecordRepository, PondsRepository pondsRepository) {
        return new UpdateHarvestRecordUseCase(harvestRecordRepository, pondsRepository);
    }

    @Bean("deleteHarvestRecordUseCase")
    public UseCase<DeleteHarvestRecordCommand, DefaultResult> deleteHarvestRecordUseCase(HarvestRecordRepository harvestRecordRepository) {
        return new DeleteHarvestRecordUseCase(harvestRecordRepository);
    }

    @Bean("viewHarvestReportUseCase")
    public UseCase<ViewHarvestReportQuery, ViewHarvestReportResult> viewHarvestReportUseCase(HarvestRecordRepository harvestRecordRepository) {
        return new ViewHarvestReportUseCase(harvestRecordRepository);
    }

    @Bean("viewFishStatisticUseCase")
    public UseCase<ViewFishStatisticQuery, ViewFishStatisticResult> viewFishStatisticUseCase(FishStatisticsRepository fishStatisticsRepository) {
        return new ViewFishStatisticUseCase(fishStatisticsRepository);
    }

    @Bean("viewGrowthChartUseCase")
    public UseCase<ViewGrowthChartQuery, ViewGrowthChartResult> viewGrowthChartUseCase(GrowthChartRepository growthChartRepository) {
        return new ViewGrowthChartUseCase(growthChartRepository);
    }

    @Bean("createWatterQualityUseCase")
    public UseCase<CreateWatterQualityCommand, CreateWatterQualityResult> createWatterQualityUseCase(WatterQualityRepository watterQualityRepository, PondsRepository pondsRepository) {
        return new CreateWatterQualityUseCase(watterQualityRepository, pondsRepository);
    }

    @Bean("viewWatterQualityUseCase")
    public UseCase<ViewWatterQualityQuery, ViewWatterQualityResult> viewWatterQualityUseCase(WatterQualityRepository watterQualityRepository, PondsRepository pondsRepository) {
        return new ViewWatterQualityUseCase(watterQualityRepository, pondsRepository);
    }

    @Bean("updateWatterQualityUseCase")
    public UseCase<UpdateWatterQualityCommand, UpdateWatterQualityResult> updateWatterQualityUseCase(WatterQualityRepository watterQualityRepository, PondsRepository pondsRepository) {
        return new UpdateWatterQualityUseCase(watterQualityRepository, pondsRepository);
    }

    @Bean("deleteWatterQualityUseCase")
    public UseCase<DeleteWatterQualityCommand, DefaultResult> deleteWatterQualityUseCase(WatterQualityRepository watterQualityRepository) {
        return new DeleteWatterQualityUseCase(watterQualityRepository);
    }

    // Supplier Use Cases
    @Bean("createSupplierUseCase")
    public UseCase<CreateSupplierCommand, CreateSupplierResult> createSupplierUseCase(SupplierRepository supplierRepository) {
        return new CreateSupplierUseCase(supplierRepository);
    }

    @Bean("viewSupplierUseCase")
    public UseCase<ViewSupplierQuery, ViewSupplierResult> viewSupplierUseCase(SupplierRepository supplierRepository) {
        return new ViewSupplierUseCase(supplierRepository);
    }

    @Bean("updateSupplierUseCase")
    public UseCase<UpdateSupplierCommand, UpdateSupplierResult> updateSupplierUseCase(SupplierRepository supplierRepository) {
        return new UpdateSupplierUseCase(supplierRepository);
    }

    @Bean("deleteSupplierUseCase")
    public UseCase<DeleteSupplierCommand, DefaultResult> deleteSupplierUseCase(SupplierRepository supplierRepository) {
        return new DeleteSupplierUseCase(supplierRepository);
    }

    // Contact Use Cases
    @Bean("createContactUseCase")
    public UseCase<CreateContactCommand, CreateContactResult> createContactUseCase(ContactRepository contactRepository) {
        return new CreateContactUseCase(contactRepository);
    }

    @Bean("viewContactUseCase")
    public UseCase<ViewContactQuery, ViewContactResult> viewContactUseCase(ContactRepository contactRepository) {
        return new ViewContactUseCase(contactRepository);
    }

    @Bean("updateContactUseCase")
    public UseCase<UpdateContactCommand, UpdateContactResult> updateContactUseCase(ContactRepository contactRepository) {
        return new UpdateContactUseCase(contactRepository);
    }

    @Bean("deleteContactUseCase")
    public UseCase<DeleteContactCommand, DefaultResult> deleteContactUseCase(ContactRepository contactRepository) {
        return new DeleteContactUseCase(contactRepository);
    }

    // Finance Record Use Cases
    @Bean("createFinanceRecordUseCase")
    public UseCase<CreateFinanceRecordCommand, CreateFinanceRecordResult> createFinanceRecordUseCase(FinanceRecordRepository financeRecordRepository) {
        return new CreateFinanceRecordUseCase(financeRecordRepository);
    }

    @Bean("viewFinanceRecordUseCase")
    public UseCase<ViewFinanceRecordQuery, ViewFinanceRecordResult> viewFinanceRecordUseCase(FinanceRecordRepository financeRecordRepository) {
        return new ViewFinanceRecordsUseCase(financeRecordRepository);
    }

    @Bean("updateFinanceRecordUseCase")
    public UseCase<UpdateFinanceRecordCommand, UpdateFinanceRecordResult> updateFinanceRecordUseCase(FinanceRecordRepository financeRecordRepository) {
        return new UpdateFinanceRecordUseCase(financeRecordRepository);
    }

    @Bean("deleteFinanceRecordUseCase")
    public UseCase<DeleteFinanceRecordCommand, DefaultResult> deleteFinanceRecordUseCase(FinanceRecordRepository financeRecordRepository) {
        return new DeleteFinanceRecordUseCase(financeRecordRepository);
    }

    @Bean("viewFinanceReportUseCase")
    public UseCase<ViewFinanceReportQuery, ViewFinanceReportResult> viewFinanceReportUseCase(FinanceRecordRepository financeRecordRepository) {
        return new ViewFinanceReportUseCase(financeRecordRepository);
    }

    // Feed Schedule Use Cases
    @Bean("createFeedScheduleUseCase")
    public UseCase<CreateFeedScheduleCommand, CreateFeedScheduleResult> createFeedScheduleUseCase(FeedScheduleRepository feedScheduleRepository, PondsRepository pondsRepository) {
        return new CreateFeedScheduleUseCase(feedScheduleRepository, pondsRepository);
    }

    @Bean("viewFeedScheduleUseCase")
    public UseCase<ViewFeedScheduleQuery, ViewFeedScheduleResult> viewFeedScheduleUseCase(FeedScheduleRepository feedScheduleRepository) {
        return new ViewFeedScheduleUseCase(feedScheduleRepository);
    }

    @Bean("updateFeedScheduleUseCase")
    public UseCase<UpdateFeedScheduleCommand, UpdateFeedScheduleResult> updateFeedScheduleUseCase(FeedScheduleRepository feedScheduleRepository, PondsRepository pondsRepository) {
        return new UpdateFeedScheduleUseCase(feedScheduleRepository, pondsRepository);
    }

    @Bean("deleteFeedScheduleUseCase")
    public UseCase<DeleteFeedScheduleCommand, DefaultResult> deleteFeedScheduleUseCase(FeedScheduleRepository feedScheduleRepository) {
        return new DeleteFeedScheduleUseCase(feedScheduleRepository);
    }

    // Feed Reminder Use Cases
    @Bean("createFeedReminderUseCase")
    public UseCase<CreateFeedReminderCommand, CreateFeedReminderResult> createFeedReminderUseCase(FeedReminderRepository feedReminderRepository, PondsRepository pondsRepository, ReminderSchedulerService reminderSchedulerService) {
        return new CreateFeedReminderUseCase(feedReminderRepository, pondsRepository, reminderSchedulerService);
    }

    @Bean("viewFeedRemindersUseCase")
    public UseCase<ViewFeedRemindersQuery, ViewFeedRemindersResult> viewFeedRemindersUseCase(FeedReminderRepository feedReminderRepository, PondsRepository pondsRepository) {
        return new ViewFeedRemindersUseCase(feedReminderRepository, pondsRepository);
    }

    @Bean("updateFeedReminderUseCase")
    public UseCase<UpdateFeedReminderCommand, UpdateFeedReminderResult> updateFeedReminderUseCase(FeedReminderRepository feedReminderRepository, PondsRepository pondsRepository, ReminderSchedulerService reminderSchedulerService) {
        return new UpdateFeedReminderUseCase(feedReminderRepository, pondsRepository, reminderSchedulerService);
    }

    @Bean("deleteFeedReminderUseCase")
    public UseCase<DeleteFeedReminderCommand, DefaultResult> deleteFeedReminderUseCase(FeedReminderRepository feedReminderRepository, ReminderSchedulerService reminderSchedulerService) {
        return new DeleteFeedReminderUseCase(feedReminderRepository, reminderSchedulerService);
    }

    // Feed Price use cases
    @Bean("createFeedPriceUseCase")
    public UseCase<CreateFeedPriceCommand, CreateFeedPriceResult> createFeedPriceUseCase(FeedPriceRepository feedPriceRepository){
        return new CreateFeedPriceUseCase(feedPriceRepository);
    }

    @Bean("viewFeedPriceUseCase")
    public UseCase<ViewFeedPriceQuery, ViewFeedPriceResult> viewFeedPriceUseCase(FeedPriceRepository feedPriceRepository){
        return new ViewFeedPriceUseCase(feedPriceRepository);
    }
    @Bean("updateFeedPriceUseCase")
    public UseCase<UpdateFeedPriceCommand, UpdateFeedPriceResult> updateFeedPriceUseCase(FeedPriceRepository feedPriceRepository){
        return new UpdateFeedPriceUseCase(feedPriceRepository);
    }

    @Bean("deleteFeedPriceUseCase")
    public UseCase<DeleteFeedPriceCommand, DeleteFeedPriceResult> deleteFeedPriceUseCase(FeedPriceRepository feedPriceRepository){
        return new DeleteFeedPriceUseCase(feedPriceRepository);
    }
}

