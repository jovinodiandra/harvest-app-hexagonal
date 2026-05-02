package org.harvest.springhttpadapter.config;

import org.harvest.BcryptPasswordSecurity;
import org.harvest.JwtConfiguration;
import org.harvest.JwtSessionManager;
import org.harvest.application.port.outbound.*;
import org.harvest.application.port.outbound.security.SessionManager;
import org.harvest.application.port.service.ReminderSchedulerService;
import org.harvest.logging.adapter.Slf4jLogAdapter;
import org.harvest.notification.adapter.ConsoleNotificationAdapter;
import org.harvest.notification.adapter.SmtpConfig;
import org.harvest.notification.adapter.SmtpNotificationAdapter;
import org.harvest.postgre.repository.*;
import org.harvest.reminder.adapter.SimpleThreadReminderSchedulerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.Duration;

@Configuration
public class DependencyConfiguration {
    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    public SessionManager sessionManager(Clock clock) {
        String signingKey = "your-secure-256-bit-secret-key-change-this-in-production!!!!";
        Duration defaultTtl = Duration.ofHours(1);
        return new JwtSessionManager(JwtConfiguration.create(signingKey, defaultTtl, clock));
    }

    @Bean
    public UserRepository userRepository() {
        return new PostgreUserRepository();
    }

    @Bean
    public OrganizationRepository organizationRepository() {
        return new PostgreOrganizationRepository();
    }

    @Bean
    public PasswordSecurity passwordSecurity() {
        return new BcryptPasswordSecurity();
    }

    @Bean
    public PondsRepository pondsRepository() {
        return new PostgrePondsRepository();
    }

    @Bean
    public SeedRepository seedRepository() {
        return new PostgreSeedRepository();
    }

    @Bean
    public GrowthRecordRepository growthRecordRepository() {
        return new PostgreGrowthRecordRepository();
    }

    @Bean
    public DeathRecordRepository deathRecordRepository() {
        return new PostgreDeathRecordRepository();
    }

    @Bean
    public DiseasesRecordRepository diseasesRecordRepository() {
        return new PostgreDiseasesRecordRepository();
    }

    @Bean
    public HarvestReminderRepository harvestReminderRepository() {
        return new PostgreHarvestReminderRepository();
    }

    @Bean
    public HarvestRecordRepository harvestRecordRepository() {
        return new PostgreHarvestRecordRepository();
    }

    @Bean
    public FishStatisticsRepository fishStatisticsRepository() {
        return new PostgreFishStatisticsRepository();
    }

    @Bean
    public GrowthChartRepository growthChartRepository() {
        return new PostgreGrowthChartRepository();
    }

    @Bean
    public WatterQualityRepository watterQualityRepository() {
        return new PostgreWatterQualityRepository();
    }

    @Bean
    public SupplierRepository supplierRepository() {
        return new PostgreSupplierRepository();
    }

    @Bean
    public ContactRepository contactRepository() {
        return new PostgreContactRepository();
    }

    @Bean
    public FinanceRecordRepository financeRecordRepository() {
        return new PostgreFinanceRecordRepository();
    }

    @Bean
    public FeedScheduleRepository feedScheduleRepository() {
        return new PostgreFeedScheduleRepository();
    }

    @Bean
    public FeedReminderRepository feedReminderRepository() {
        return new PostgreFeedReminderRepository();
    }

    @Bean
    public FeedPriceRepository feedPriceRepository(){
        return new PostgreFeedPriceRepository();
    }

    @Bean
    public LogManager logManager() {
        return new Slf4jLogAdapter();
    }

    @Bean
    public SmtpConfig smtpConfig() {
        // For production, use environment variables or external config
        return SmtpConfig.create(
                "smtp-relay.brevo.com",
                587,
                "a883d3001@smtp-brevo.com",
                "xsmtpsib-55f8f57c1b3d714b7b430f368f231b155d8085dd31240fd894161f29540af00d-v1mMbKNsqRNxZCKB",
                "a883d3001@smtp-brevo.com",
                "Harvest App"
        );
    }

    @Bean
    public NotificationSender notificationSender(
            LogManager logManager
    ) {
        // Use SmtpNotificationAdapter for production
        return new ConsoleNotificationAdapter( logManager);

        // Use ConsoleNotificationAdapter for development/testing
        // return new ConsoleNotificationAdapter(logManager);
    }

    @Bean
    public ReminderSchedulerService reminderSchedulerService(
            NotificationSender notificationSender,
            LogManager logManager
    ) {
        return new SimpleThreadReminderSchedulerAdapter(notificationSender, logManager);
    }
}
