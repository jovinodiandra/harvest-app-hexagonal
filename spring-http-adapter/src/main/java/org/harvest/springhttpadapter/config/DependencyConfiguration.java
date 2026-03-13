package org.harvest.springhttpadapter.config;

import org.harvest.BcryptPasswordSecurity;
import org.harvest.JwtConfiguration;
import org.harvest.JwtSessionManager;
import org.harvest.application.port.outbound.*;
import org.harvest.application.port.outbound.security.SessionManager;
import org.harvest.postgre.repository.PostgreContactRepository;
import org.harvest.postgre.repository.PostgreDeathRecordRepository;
import org.harvest.postgre.repository.PostgreFeedReminderRepository;
import org.harvest.postgre.repository.PostgreFeedScheduleRepository;
import org.harvest.postgre.repository.PostgreFinanceRecordRepository;
import org.harvest.postgre.repository.PostgreFishStatisticsRepository;
import org.harvest.postgre.repository.PostgreGrowthChartRepository;
import org.harvest.postgre.repository.PostgreGrowthRecordRepository;
import org.harvest.postgre.repository.PostgreDiseasesRecordRepository;
import org.harvest.postgre.repository.PostgreHarvestRecordRepository;
import org.harvest.postgre.repository.PostgreHarvestReminderRepository;
import org.harvest.postgre.repository.PostgreOrganizationRepository;
import org.harvest.postgre.repository.PostgrePondsRepository;
import org.harvest.postgre.repository.PostgreSeedRepository;
import org.harvest.postgre.repository.PostgreSupplierRepository;
import org.harvest.postgre.repository.PostgreUserRepository;
import org.harvest.postgre.repository.PostgreWatterQualityRepository;
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
}
