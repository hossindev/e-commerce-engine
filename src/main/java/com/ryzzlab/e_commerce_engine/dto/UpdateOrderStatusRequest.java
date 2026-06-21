package com.ryzzlab.e_commerce_engine.dto;

import com.ryzzlab.e_commerce_engine.entity.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOrderStatusRequest {
    Status newStatus;
}
