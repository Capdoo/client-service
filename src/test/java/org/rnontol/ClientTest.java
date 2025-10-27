package org.rnontol;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.rnontol.controller.ClientController;
import io.quarkus.panache.mock.PanacheMock;
import org.rnontol.entity.Client;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

//import io.quarkus.runtime.configuration.ProfileManager;

@QuarkusTest
public class ClientTest {

    @Inject
    ClientController clientController;

    @BeforeEach
    void setup() {
        PanacheMock.mock(Client.class);
    }

    @Test
    void testGetClient() {
        // * Arrange
        Client client = new Client(1L, "John", "Sevillano","74921641", 30, "Regular");
        //Mockito.when(customerRepository.findAllCustomers()).thenReturn(List.of(customer2));
        Mockito.when(Client.listAll()).thenReturn(List.of(client));

        List<Client> expected = List.of(client);

        // * Act
        List<Client> response = clientController.getClients();

        // * Assert
        assertAll(
                () -> Assertions.assertEquals(expected, response),
                () -> org.assertj.core.api.Assertions.assertThat(response).usingRecursiveComparison().isEqualTo(expected)
        );
    }

    @Test
    void testGetClientById() {
        // * Arrange
        Client client = new Client(1L, "John", "Sevillano","74921641", 30, "Regular");
        Mockito.when(Client.findById(anyLong())).thenReturn(client);

        // * Act
        Client response = clientController.getClient(1L);

        // * Assert
        assertAll(
                () -> assertThat(response).usingRecursiveComparison().isEqualTo(client)
        );
    }

    @Test
    void testDeleteClient() {
        // * Arrange
        Client client = new Client(1L, "John", "Sevillano","74921641", 30, "Regular");
        Mockito.when(Client.findById(1L)).thenReturn(client);

        // * Act
        clientController.deleteClient(1L);

        // * Assert
        PanacheMock.verify(Client.class, times(1)).deleteById(client.id);
    }


    @Test
    void testDeleteCustomerNotFound() {
        // * Arrange
        Mockito.when(Client.findById(1L)).thenReturn(null);

        // * Act & Assert
        // * Option 1
        try {
            clientController.deleteClient(1L);
            fail();
        } catch (Exception e) {
            assertThat(e).isInstanceOf(NotFoundException.class);
        }

        // * Option 2
        assertThrows(ClientErrorException.class, () -> clientController.deleteClient(1L));
        assertThrowsExactly(NotFoundException.class, () -> clientController.deleteClient(1L));

        // * Option 3
        assertThatThrownBy(() -> clientController.deleteClient(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Client not found");

        // * Option 4
        Exception exception = assertThrows(NotFoundException.class, () -> clientController.deleteClient(1L));
        assertAll(
                () -> assertThat(exception)
                        .isInstanceOf(NotFoundException.class)
                        .extracting(Exception::getMessage)
                        .isEqualTo("Client not found with id: 1")
        );

    }

}