/*
 * Copyright (C) 2018 José Ramón Cuevas  https://www.linkedin.com/in/joseramoncuevasdiez
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package criptolabhash.control;

/**
 * Operaciones tanto de mando como de consulta que se pueden realizar en el
 * servidor.
 *
 * @author José Ramón Cuevas https://www.linkedin.com/in/joseramoncuevasdiez
 */
public interface APIServerHash {

    /**
     * Resetea los clientes y setea el nuevo algoritmo, luego los arranca.
     *
     * @param algoritmo Algoritmo a usar en el siguiente ataque.
     * @param sizemsg Tamaño del mensaje en bytes.
     * @param sizehash Tamaño del hash en bytes.
     */
    public void setAlgoritmo(String algoritmo, int sizemsg, int sizehash);

    /**
     * Comienza el cálculo de hash y mensajes aleatorios en los clientes.
     */
    public void arrancaAtaque();

    /**
     * Para momentaneamente la ejecución del ataque.
     */
    public void paraAtaque();

    /**
     * Desconecta los clientes.
     */
    public void desconectaClientes();

    /**
     * Para la ejecución de los clientes y elimina las bases de datos de los
     * clientes y del servidor.
     */
    public void resetearSistema();

    /**
     * Consulta el nombre del algoritmo seleccionado.
     *
     * @return Nombre del algoritmo.
     */
    public String getHashName();

    /**
     * Consulta el tamaño de los mensajes que han de generar los clientes.
     *
     * @return Tamaño del mensaje en bytes.
     */
    public int getSizeMsg();

    /**
     * Consulta el tamaño de los hash calculados en los clientes.
     *
     * @return Tamaño del hash en bytes.
     */
    public int getSizeHash();

    /**
     * Consulta el número de clientes conectados.
     *
     * @return Número de clientes conectados.
     */
    public int nClientesConectados();

    /**
     * Consulta el número de hash calculados hasta ahora.
     *
     * @return Número de Hashes calculados.
     */
    public long nHashCalculados();

    /**
     * Consulta el número de colisiones encontradas hasta ahora.
     *
     * @return Número de colisiones encontradas.
     */
    public int nColisiones();

    /**
     * Consulta el número de mensajes iguales, generado por los clientes. Esto
     * no son verdaderas colisiones, puesto que provienen del mismo mensaje.
     *
     * @return Número de pseudocolisiones.
     */
    public int nPseudoColisiones();

    /**
     * Genera un informe con todos los hash encontrados, que son generados por
     * dos o más mensajes.
     *
     * @return Informe en formato texto plano de 64 caracteres de ancho.
     */
    public String getInformeColisiones();

    /**
     * Consulta en que ip y puerto esta escuchando el servidor.
     *
     * @return Texto con ip y puerto.
     */
    public String getInfoConexion();
}
