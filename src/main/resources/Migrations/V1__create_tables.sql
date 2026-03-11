CREATE TABLE optimization_requests (
    id UUID PRIMARY KEY,
    max_volume INT NOT NULL,
    total_volume INT NOT NULL,
    total_revenue INT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE selected_shipments (
    id BIGSERIAL PRIMARY KEY,
    optimization_request_id UUID NOT NULL,
    shipment_name VARCHAR(255) NOT NULL,
    volume INT NOT NULL,
    revenue INT NOT NULL,
    CONSTRAINT fk_selected_shipments_request
        FOREIGN KEY (optimization_request_id)
        REFERENCES optimization_requests(id)
        ON DELETE CASCADE
);