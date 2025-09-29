package com.hs.lab1.entity;

import com.hs.lab1.enums.GroupEventStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "group_events")
public class GroupEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 30)
    private String name;

    @Size(max = 200)
    private String description;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    @ManyToMany
    @JoinTable(
            name = "user_group_events",
            joinColumns = @JoinColumn(name = "group_event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> participants;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @Enumerated(EnumType.STRING)
    private GroupEventStatus status;
}
