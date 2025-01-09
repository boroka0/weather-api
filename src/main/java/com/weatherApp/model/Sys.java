package com.weatherApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sys {
    private long sunrise;
    private long sunset;
}

