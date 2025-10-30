package SDD.smash.Dwelling.Dto;

public class ResponseDTO {
    private boolean success;
    private String message;

    public ResponseDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
