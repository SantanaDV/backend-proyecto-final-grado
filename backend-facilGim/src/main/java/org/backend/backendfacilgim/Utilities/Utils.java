package org.backend.backendfacilgim.Utilities;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Utils {

    /**
     * Método auxiliar para construir mensajes de error
     * cuando hay fallos en la validación (@Valid).
     * @param result BindingResult que contiene los errores de validación.
     * @return Respuesta con mapa de errores (campo->mensaje).
     */
    public static ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }


    /**
     * Comprueba que el string contiene número y letras y ademas al menos un caracter especial.
     *
     * @param cadena
     * @return boolean
     */
    public static boolean validarCadena(String cadena) {
        String regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z\\d]).+$";
        return cadena.matches(regex);
    }


    /**
     * Comprueba que el string contiene mas de 6 caracteres
     *
     * @param contrasena
     * @return boolean
     */
    public static boolean stringMayorASeisCaracteres(String contrasena) {
        return contrasena.length() >= 6;
    }

    /**
     * Comprueba que el string contiene un numero mediante el metodo equals
     * @param cadena1
     * @param cadena2
     * @return boolean
     */
    public static boolean comprobarCadenasIguales(String cadena1, String cadena2) {
        return cadena1.equals(cadena2);
    }

    /**
     * Comprueba que el email tiene el formato correcto
     * @param email
     * @return boolean
     */
    public static boolean comprobarFormatoCorreo(String email) {
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return Pattern.matches(regex, email);
    }
}
