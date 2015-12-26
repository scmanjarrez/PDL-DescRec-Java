/*Seccion de codigo de usuario*/
package analizadorlex;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

//clase de los token devueltos
class Yytoken {
    Yytoken (int numToken,String token, String tipo, int linea, int columna){
        //Contador para el número de tokens reconocidos
        this.numToken = numToken;
        //String del token reconocido
        this.token = new String(token);
        //Tipo de componente léxico encontrado
        this.tipo = tipo;
        //Número de linea
        this.linea = linea;
        //Columna donde empieza el primer carácter del token
        this.columna = columna;
    }
    //Métodos de los atributos de la clase
    public int numToken;
    public String token;
    public String tipo;
    public int linea;
    public int columna;
    //Metodo que devuelve los datos necesarios que escribiremos en un archive de salida
    public String toString() {
        return "< " + tipo + " , " + token + " >";
    }
}

/* Seccion de opciones y declaraciones de JFlex */
%% //inicio de opciones
//Cambiamos el nombre la funcion para el siguiente token por nextToken
%function nextToken
//Clase publica
%public
//Cambiamos el nombre de la clase del analizador
%class AnalizadorLexico
//Agregamos soporte a unicode
%unicode
//Codigo java
%{

    private int contador;
    private ArrayList<Yytoken> tokens;

	private void writeOutputFile() throws IOException{
			String filename = "file.out";
			BufferedWriter out = new BufferedWriter(
				new FileWriter(filename));
            System.out.println("\n*** Tokens guardados en archivo ***\n");
			for(Yytoken t: this.tokens){
				System.out.println(t);
				out.write(t + "\n");
			}
			out.close();
	}
%}
//Creamos un contador para los tokens
%init{
    contador = 0;
	tokens = new ArrayList<Yytoken>();
%init}
//Cuando se alcanza el fin del archivo de entrada
%eof{
	try{
		this.writeOutputFile();
        System.exit(0);
	}catch(IOException ioe){
		ioe.printStackTrace();
	}
%eof}
//Activar el contador de lineas, variable yyline
%line
//Activar el contador de columna, variable yycolumn
%column
//Fin de opciones

//Expresiones regulares
//Declaraciones
EXP_ALPHA=[A-Za-z]
EXP_DIGITO=[0-9]
EXP_ALPHANUMERIC={EXP_ALPHA}|{EXP_DIGITO}
NUMERO=({EXP_DIGITO})+
IDENTIFICADOR={EXP_ALPHA}({EXP_ALPHANUMERIC})*
ESPACIO=" "|\t
SALTO=\n|\r|\r\n
PUNTOYCOMA=";"
COMA=","
ABRPARENT="("
CIEPARENT=")"
ABRLLAVE="{"
CIELLAVE="}"
SUMA="+"
ASIG="="
IGUALDAD="=="
ANDLOG="&&"
ASIGMOD="%="
BOTRUE="true"
BOFALS="false"
COMMENT="/*" ~"*/"
CADENA="\"" ~"\""



//fin declaraciones

/* Seccion de reglas lexicas */
%%
//Regla     {Acciones}

{COMA}    {
    contador++;
    Yytoken t = new Yytoken(contador,yytext(),"Coma",yyline,yycolumn);
    tokens.add(t);
    return t;
}
{PUNTOYCOMA}    {
    contador++;
    Yytoken t = new Yytoken(contador,yytext(),"PtComa",yyline,yycolumn);
    tokens.add(t);
    return t;
}

{BOTRUE}    {
    contador++;
    Yytoken t = new Yytoken(contador,"1","Boolean",yyline,yycolumn);
    tokens.add(t);
    return t;
}

{BOFALS}    {
    contador++;
    Yytoken t = new Yytoken(contador,"0","Boolean",yyline,yycolumn);
    tokens.add(t);
    return t;
}

"var"    {
    contador++;
    Yytoken t = new Yytoken(contador,"var","PalReserv",yyline,yycolumn);
    tokens.add(t);
    return t;
}

"int"    {
    contador++;
    Yytoken t = new Yytoken(contador,"int","PalReserv",yyline,yycolumn);
    tokens.add(t);
    return t;
}




"bool"    {
    contador++;
    Yytoken t = new Yytoken(contador,"bool","PalReserv",yyline,yycolumn);
    tokens.add(t);
    return t;
}

"char"    {
    contador++;
    Yytoken t = new Yytoken(contador,"char","PalReserv",yyline,yycolumn);
    tokens.add(t);
    return t;
}

"write"    {
    contador++;
    Yytoken t = new Yytoken(contador,"write","PalReserv",yyline,yycolumn);
    tokens.add(t);
    return t;
}

"prompt"    {
    contador++;
    Yytoken t = new Yytoken(contador,"prompt","PalReserv",yyline,yycolumn);
    tokens.add(t);
    return t;
}

"return"    {
    contador++;
    Yytoken t = new Yytoken(contador,"return","PalReserv",yyline,yycolumn);
    tokens.add(t);
    return t;
}

"if"    {
    contador++;
    Yytoken t = new Yytoken(contador,"if","PalReserv",yyline,yycolumn);
    tokens.add(t);
    return t;
}

"for"    {
    contador++;
    Yytoken t = new Yytoken(contador,"for","PalReserv",yyline,yycolumn);
    tokens.add(t);
    return t;
}

"break"    {
    contador++;
    Yytoken t = new Yytoken(contador,"break","PalReserv",yyline,yycolumn);
    tokens.add(t);
    return t;
}




{CADENA}   {
    contador++;
    Yytoken t = new Yytoken(contador,yytext(),"Cadena",yyline,yycolumn);
    tokens.add(t);
    return t;
}

{IDENTIFICADOR}   {
    contador++;
    Yytoken t = new Yytoken(contador,yytext(),"Id",yyline,yycolumn);
    tokens.add(t);
    return t;
}

{ABRPARENT}   {
    contador++;
    Yytoken t = new Yytoken(contador,yytext(),"ParAb",yyline,yycolumn);
    tokens.add(t);
    return t;
}

{CIEPARENT}   {
    contador++;
    Yytoken t = new Yytoken(contador,yytext(),"ParCer",yyline,yycolumn);
    tokens.add(t);
    return t;
}

{ABRLLAVE}   {
    contador++;
    Yytoken t = new Yytoken(contador,yytext(),"LlavAb",yyline,yycolumn);
    tokens.add(t);
    return t;
}

{CIELLAVE}   {
    contador++;
    Yytoken t = new Yytoken(contador,yytext(),"LlavCer",yyline,yycolumn);
    tokens.add(t);
    return t;
}

{SUMA}  {
    contador++;
    Yytoken t = new Yytoken(contador,yytext(),"OpAritSum",yyline,yycolumn);
    tokens.add(t);
    return t;
}

{IGUALDAD}  {
    contador++;
    Yytoken t = new Yytoken(contador,yytext(),"OpRelacIgual",yyline,yycolumn);
    tokens.add(t);
    return t;
}

{ANDLOG}  {
    contador++;
    Yytoken t = new Yytoken(contador,yytext(),"OpLogAnd",yyline,yycolumn);
    tokens.add(t);
    return t;
}

{ASIGMOD}  {
    contador++;
    Yytoken t = new Yytoken(contador,yytext(),"OpAsigMod",yyline,yycolumn);
    tokens.add(t);
    return t;
}

{ASIG}  {
    contador++;
    Yytoken t = new Yytoken(contador,yytext(),"OpAsig",yyline,yycolumn);
    tokens.add(t);
    return t;
}

{NUMERO}    {
    contador++;
    Yytoken t = new Yytoken(contador,yytext(),"Num",yyline,yycolumn);
    tokens.add(t);
    return t;
}

{ESPACIO} {
 	//ignorar
}

{SALTO} {
    contador++;
    Yytoken t = new Yytoken(contador,"","eol",yyline,yycolumn);
    tokens.add(t);
    return t;
}
{COMMENT} {
  //ignorar
}

<<EOF>> {
  contador++;
  Yytoken t = new Yytoken(contador,"","eof",yyline,yycolumn);
  tokens.add(t);
  return t;
}
