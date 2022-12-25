package com.example.ordersystem.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {
    @Id
    private String id;

    @Column(name = "origin_latitude", nullable = false)
    private String originLatitude;

    @Column(name = "origin_longitude", nullable = false)
    private String originLongitude;

    @Column(name = "destination_latitude", nullable = false)
    private String destinationLatitude;

    @Column(name = "destination_longitude", nullable = false)
    private String destinationLongitude;

    @Column(name = "distance", nullable = false)
    private Integer distance;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "created_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime createdTime;

    @Column(name = "updated_time")
    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime updatedTime;

    public enum Status {
        UNASSIGNED,
        ASSIGNED,
        TAKEN
    }
}
