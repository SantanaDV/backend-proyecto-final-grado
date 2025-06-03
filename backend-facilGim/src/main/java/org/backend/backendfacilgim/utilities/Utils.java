package org.backend.backendfacilgim.utilities;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Clase utilitaria con métodos de ayuda para validaciones y construcción
 * de respuestas de error en controladores.
 *
 * Autor: Francisco Santana
 */
public class Utils {

    /**
     * Construye una respuesta de error HTTP 400 con los mensajes
     * de validación extraídos de {@link BindingResult}.
     *
     * @param result Resultado de validación que contiene errores de campos.
     * @return ResponseEntity con un mapa campo→mensaje describiendo cada error.
     */
    public static ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errors.put(
                    err.getField(),
                    "El campo " + err.getField() + " " + err.getDefaultMessage()
            );
        });
        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * Comprueba que la cadena contenga al menos una letra, al menos un dígito
     * y al menos un carácter especial (no alfanumérico).
     *
     * @param cadena Cadena de texto a validar.
     * @return true si cumple con la expresión (letra + número + especial), false en caso contrario.
     */
    public static boolean validarCadena(String cadena) {
        String regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z\\d]).+$";
        return cadena.matches(regex);
    }

    /**
     * Comprueba que la longitud de la contraseña sea al menos de seis caracteres.
     *
     * @param contrasena Cadena que representa la contraseña.
     * @return true si la longitud ≥ 6, false si es menor.
     */
    public static boolean stringMayorASeisCaracteres(String contrasena) {
        return contrasena != null && contrasena.length() >= 6;
    }

    /**
     * Comprueba si dos cadenas de texto son exactamente iguales.
     *
     * @param cadena1 Primera cadena a comparar.
     * @param cadena2 Segunda cadena a comparar.
     * @return true si ambas cadenas coinciden en contenido, false en caso contrario.
     */
    public static boolean comprobarCadenasIguales(String cadena1, String cadena2) {
        return cadena1 != null && cadena1.equals(cadena2);
    }

    /**
     * Valida que el formato de email sea correcto según patrón básico
     * <code>texto@texto.dominio</code>.
     *
     * @param email Dirección de correo a validar.
     * @return true si coincide con el patrón, false en caso contrario.
     */
    public static boolean comprobarFormatoCorreo(String email) {
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email != null && Pattern.matches(regex, email);
    }
}
