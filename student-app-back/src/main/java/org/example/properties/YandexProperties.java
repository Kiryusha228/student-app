package org.example.properties;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class YandexProperties {
  String apiUrl;
  String apiKey;
  String folderId;
  String commandPathForDocker;
  String commandPathForIde;
  String model;

  YandexProperties() {
    this.apiUrl = System.getenv("YANDEX_API_URL");
    this.apiKey = System.getenv("YANDEX_API_KEY");
    this.folderId = System.getenv("YANDEX_FOLDER_ID");
    this.commandPathForDocker = System.getenv("YANDEX_COMMAND_PATH_FOR_DOCKER");
    this.commandPathForIde = System.getenv("YANDEX_COMMAND_PATH_FOR_IDE");
    this.model = System.getenv("YANDEX_MODEL");
  }
}
