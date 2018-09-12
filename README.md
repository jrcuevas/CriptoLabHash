# CriptoLabHash

Herramienta didáctica sobre funciones hash y ataque distribuido mediante la 
paradoja del cumpleaños. La herramienta consta de dos módulos, uno para el 
cálculo y seguimiento de las operaciones hash, y otro para experimentar con la 
búsqueda de colisiones.

En la versión actual se pueden obtener hash mediante los algoritmos MD5 y SHA1. 
Las posibles fuentes de entrada son; texto plano, texto hexadecimal, y archivos 
sin límite de tamaño. El límite lo pone los recursos de la máquina en la que se 
ejecuta. La salida puede ser un informe de seguimiento de las operaciónes 
realizadas por estos algoritmos, tanto a nivel de paso a paso, como a nivel de 
bloque.

La parte de experimentación con la búsqueda de colisiones permite elegir 
solamente una parte del hash calculado, en lugar de buscar una coincidencia 
completa, para adaptarlo a la potencia de procesamiento disponible. Así mismo se
 pueden arrancar varios módulos clientes que ayuden a la búsqueda de los mensajes
 cuyos hash colisionen. 

## Empezando

Este proyecto está desarrollado enteramente en lenguaje Java, y con ayuda del 
IDE Netbeans, versión 8.2. Si quiere realizar una copia de los fuentes en su máquina, 
puede descargarlos de la siguiente dirección "https://github.com/jrcuevas/CriptoLabHash.git" 
Si solo desea experimentar con la herramienta dispone de los ejecutables en la 
carpeta /distribucion/.

### Prerrequisitos

Este software puede correr en cualquier máquina que disponga de una JVM 1.8 o 
superior, y al menos 1GB de ram diponible para cada instancia de la aplicación.
La aplicación consta de un módulo servidor, que es el que arranca el interface 
gráfico y un módulo cliente que puede ser ejecutado en varios equipos 
simultaneamente para proveer de mayor potencia de cálculo. Como requisito 
indispensable es, que el equipo donde se ejecute el servidor, deje entrar 
comunicaciones por la ip y el puerto donde el servidor esté escuchando. La ip y 
el puerto de escucha, lo podemos obtener accediendo a la pestaña de ataques. 
También es indispensable que los equipos donde esten instalados los clientes 
tengan acceso a la ip y puerto del servidor.

## Despliegue

En la carpeta /distribucion/ se encuentran los dos ejecutables JAR de la herramienta.
Este proyecto consta de dos módulos ejecutables. Dependiendo del uso que se le 
vaya a dar, sólamente será necesario arrancar el servidor.
Para ejecutar la herramienta de software y experimentar con el cálculo de hash y
 seguimiento de las funciones, solo necesita ejecutar el servidor haciendo doble
click sobre el archivo "CriptoLabHashServidor.jar". En caso de que no se ejecute
 automáticamente, será necesario arrancarlo desde un consola, de la siguiente 
forma:

```
java -jar CriptoLabHashServidor.jar
```

Para la experimentación con la búsqueda de colisiones, se debe arrancar el o los 
módulos cliente desde consola y despues de haber arrancado el servidor. Por 
ejemplo si el servidor esta escuchando en la dirección 192.168.0.14 y puerto 
10001, deberemos arrancar los clientes de la siguiente forma:

```
java -jar CriptoLabHashCliente.jar 192.168.0.14 10001
```

En caso de que tuvieramos algún conflicto con el puerto del servidor, este se 
puede arrancar indicándole también la ip y puerto sugerido.

## Construido con

* [Netbeans] (https://netbeans.org) IDE de programación.

# Versioneado

Se usa [GitHub] (https://github.com) para el control de versiones.

## Autor

* **José Ramón Cuevas**

## License

Este proyecto está licenciado bajo la licencia GNU GENERAL PUBLIC LICENSE 
Versión 3. Consulte el archivo [LICENSE] (LICENSE) para obtener más detalles.

## Agradecimientos

* Esta herramienta se ha desarrollado bajo el proyecto de TFM perteneciente al 
**Master Universitario en Seguridad Informática** cursado durante el año 2018 
en la **Universidad Internacional de la Rioja**, por el alumno **D. José Ramón 
Cuevas** y teniendo como director al profesor **D. José Luis Lucas Simarro**.
* Este proyecto esta inspirado en la herramienta CriptoRES desarrollada por el 
alumno *D. José Azaña Alonso*, siendo su tutor el profesor *D. Jorge Ramió Aguirre*.
