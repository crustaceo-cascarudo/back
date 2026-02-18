package com.fpmislata.back.domain.enumerado;

public enum Estado {
  CARRITO, PENDIENTE, PAGADO, ENVIADO, CANCELADO;

  public static Estado fromString(String estado) {
    if (estado == null) {
      throw new IllegalArgumentException("Estado no puede ser nulo");
    }
    try {
      return Estado.valueOf(estado.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(
          "Estado inválido: " + estado + ". Debe ser PENDIENTE, PAGADO, ENVIADO, ENTREGADO o CANCELADO");
    }
  }
}
