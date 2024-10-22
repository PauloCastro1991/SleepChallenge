package com.noom.interview.fullstack.models;

import com.noom.interview.fullstack.enums.Mood;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sleep_logs")
public class SleepLog {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sleep_log_id_seq")
    @SequenceGenerator(name = "sleep_log_id_seq", sequenceName = "sleep_log_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "sleep_start", nullable = false)
    private LocalDateTime sleepStart;

    @Column(name = "sleep_end", nullable = false)
    private LocalDateTime sleepEnd;

    @Column
    @Enumerated(EnumType.STRING)
    private Mood mood;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}