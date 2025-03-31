package com.kitchensaver.backend.Repo;

import com.kitchensaver.backend.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

// JobRepo is an interface that extends JpaRepository to handle database operations for the Job entity
public interface JobRepo extends JpaRepository<Job, Long> {
    
    // Method to find all jobs assigned to a specific cabinet maker using their ID
    List<Job> findByCabinetMakerId(Long id);
    
    // Method to find all jobs assigned to a specific installer using their ID
    List<Job> findByInstallerId(Long id);
    
    // Custom query method to filter jobs based on multiple optional criteria
    @Query("SELECT j FROM Job j WHERE " +
            "(:status IS NULL OR j.status = :status) AND " +
            "(:installerId IS NULL OR j.installer.id = :installerId) AND " +
            "(:materialOrderStatus IS NULL OR j.materialOrderStatus = :materialOrderStatus) AND " +
            "(:materialArrivalStatus IS NULL OR j.materialArrivalStatus = :materialArrivalStatus) AND " +
            "(:office IS NULL OR j.office = :office)")
    List<Job> findByFilters(
            @Param("status") String status, // Filters jobs by status if provided
            @Param("installerId") Long installerId, // Filters jobs by installer ID if provided
            @Param("materialOrderStatus") String materialOrderStatus, // Filters jobs by material order status if provided
            @Param("materialArrivalStatus") String materialArrivalStatus, // Filters jobs by material arrival status if provided
            @Param("office") String office // Filters jobs by office location if provided
    );
}
