package com.ecomerce.sportscenter.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Response {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long idResponse;
    @OneToOne
    Complaint complaint;
    String descResponse;

}