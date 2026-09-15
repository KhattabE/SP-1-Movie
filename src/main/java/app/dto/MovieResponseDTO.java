package app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MovieResponseDTO {

    private int page;

    private List<MovieDTO> results;

    @JsonProperty("total_pages")
    private int totalPages;
}