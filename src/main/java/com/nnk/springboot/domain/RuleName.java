package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "rule_name")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "name", "description"})
public class RuleName {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Integer id;

    @NotBlank(message = "Name is mandatory")
    @Size(max = 125)
    @Column(nullable = false, length = 125)
    private String name;

    @Column(length = 125)
    private String description;

    @Column(length = 125)
    private String json;

    @Column(length = 512)
    private String template;

    @Column(length = 125)
    private String sqlStr;

    @Column(length = 125)
    private String sqlPart;

    public RuleName(String name, String description, String json, String template, String sqlStr, String sqlPart) {
        this.name = name;
        this.description = description;
        this.json = json;
        this.template = template;
        this.sqlStr = sqlStr;
        this.sqlPart = sqlPart;
    }
}
