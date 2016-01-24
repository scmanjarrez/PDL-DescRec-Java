package pruebas;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
@SuppressWarnings("all")

/**
 *
 * @author Sergio Chica Manjarrez
 * @author Ana�s Querol Cruz
 * @author Silvia Sebasti�n Gonz�lez
 * 
 */

/**
 * 
	 private Yytoken zzDoEOF() {
	  Yytoken t=null;
    	if (!zzEOFDone) {
      		zzEOFDone = true;
    		try{
	    		contador++;
	    		t = new Yytoken(contador,"","eof",yyline,yycolumn);
	    		tokens.add(t);
				this.escribirEnFichero();
				return t;
			}catch(IOException ioe){
				ioe.printStackTrace();
			}
    	}
    	return t;
  	}


	if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
     	zzAtEOF = true;
        Yytoken t = zzDoEOF();
        return t;
    }

     private void escribirEnFichero() throws IOException{
	 	String filename = "Tokens.txt";
		BufferedWriter out = new BufferedWriter(new FileWriter(filename));
		for(Yytoken t: this.tokens){
			out.write(t + "\n");
		}
		out.close();
		System.out.println("Fichero Tokens.txt generado"); 
	}

 * 
 * **/
public class Main {

	static Yytoken tokenActual = null;
	static AnalizadorLexico a = null;
	static ArrayList<Integer> parser = new ArrayList<Integer>();

	static BufferedWriter error;

	//Genero la estructura que almacenara las tablas
	static ArrayList <ArrayList> tablas = new ArrayList <ArrayList>();
	static Integer tablaActual = 0; //indica en que tabla nos encontramos

	static boolean soloLlamada = false;

	public static void main(String[] args) {
		String entrada = "";

		java.util.Scanner in = new java.util.Scanner(System.in);

		System.out.println("Por favor, escriba el nombre del fichero, \npuede hacerlo mediante un path relativo/absoluto.\n");
		System.out.print("Nombre fichero: ");
		entrada = in.next();
		System.out.println("");
		BufferedReader bf = null;

		try {
			bf = new BufferedReader(new FileReader(entrada));
			try{
				error = new BufferedWriter(new FileWriter("Errores.txt"));
			} catch (FileNotFoundException ex){
				System.err.println("No se puede generar el fichero Errores.txt \ncompruebe que tiene permisos de escritura \nen el directorio de trabajo");
			}
			a = new AnalizadorLexico(bf);
			try {
				tokenActual = a.sigToken();
			} catch (IOException e1) {
				System.err.println("Error de entrada/salida durante el an�lisis del fichero.");
			}
			Pp();
			bf.close();
			generarParser();
			generarTS();
			error.close();
			System.out.println("Fichero Errores.txt generado.");
		} catch (FileNotFoundException e1) {
			System.err.println("Fichero descrito no encontrado, o corrupto.");
		} catch (IOException e) {
			System.err.println("Error de entrada/salida.");
		} catch (Exception x){
			System.err.println("Sintaxis incorrecta en l�nea("+linea()+"). Car�cter \""+tokenActual.token+"\" no esperado.\n\n");//luego se quita
		}

	}

	public static void errorAFichero(String msg){
		try{
			error.write(msg+"\n");
		}catch (IOException e) {
			System.err.println("Error de entrada/salida.");
		}
	}

	public static void generarTS() {
		String filename = "TS.txt";
		BufferedWriter out;
		try {
			out = new BufferedWriter(new FileWriter(filename));
			System.out.println("Fichero TS.txt generado");
			for ( int t=0; t<tablas.size(); t++ ) {
				out.write("CONTENIDO DE LA TABLA # "+t+":\n");
				for ( int e=1; e<tablas.get(t).size(); e++ ) {
					out.write("  * LEXEMA    : \'"+((Entrada)tablas.get(t).get(e)).getNom()+"\' :\n" );
					out.write("    ATRIBUTOS : ");
					if ( ((Entrada)tablas.get(t).get(e)).getTipo().equals("fun") ) {
						out.write(" (funcion)\n");
						out.write("    + tipo    : \'"+((Entrada)tablas.get(t).get(e)).getRet()+"\'\n" );

						int a = 1;
						for ( String s : ((Entrada)tablas.get(t).get(e)).printParam() ) {
							out.write("    + param " + a + " : \'" + s +"\'\n");
							a++;
						} 
						if ( a==1 ) 
							out.write("    + param   : \'\' (no recibe parametros)\n");

					} else {
						out.write("\n    + tipo    : \'"+((Entrada)tablas.get(t).get(e)).getTipo()+"\'\n" );
						out.write("    + despl   : "+((Entrada)tablas.get(t).get(e)).getDespl()+"\n");
					}
					out.write("\n");
				}
				out.write("----------------------------------------------------\n\n");
			}out.close();
		}catch(Exception e){};
	}

	public static void generarParser(){
		String filename = "Parser.txt";
		BufferedWriter out;
		try{
			out = new BufferedWriter(new FileWriter(filename));
			out.write("Des ");
			int num;
			for(int i=0; i<parser.size();i++){
				num = parser.get(i);
				out.write(num+" ");
			}
			out.close();
			System.out.println("Fichero Parser.txt generado.");
		}catch(FileNotFoundException ex){
			System.err.println("No se puede generar el fichero Parser.txt \ncompruebe que tiene permisos de escritura \nen el directorio de trabajo.");
		}catch (IOException e) {
			System.err.println("Error de entrada/salida.");
		}
	}

	public static boolean tokenActualIgual(String token){
		if(token.equals("eol")
				|| token.equals("eof")
				|| token.equals("Id")
				|| token.equals("Num")
				|| token.equals("Cadena")){
			return tokenActual.tipo.equals(token);

		}else
			return tokenActual.token.equals(token);
	}
	public static void compToken(String token){
		if(tokenActualIgual(token)){
			try{
				tokenActual=a.sigToken();
			}catch (Exception ex) {
				System.err.println("ERROR L�XICO  (l�nea "+linea()+"): No se reconoce el s�mbolo en la gram�tica, despu�s del s�mbolo \""+tokenActual.token+"\"");
				System.exit(0);
			}

		}else
			throw new RuntimeException("El token recibido por el An.L�xico no coincide con el esperado.");
	}


	public static void Pp(){//[1] 			P' -> P
		//creo una nueva tabla de simbolos
		//creo la primera tabla de simbolos
		ArrayList <Entrada> TS = new ArrayList <Entrada>();
		TS.add(new Entrada (0, "null", "null", 0, null, "null", null));
		tablas.add(TS);

		if(tokenActualIgual("eol")
				|| tokenActualIgual("eof")
				|| tokenActualIgual("var")
				|| tokenActualIgual("if") 
				|| tokenActualIgual("for") 
				|| tokenActualIgual("Id")
				|| tokenActualIgual("return")
				|| tokenActualIgual("write")
				|| tokenActualIgual("prompt")
				|| tokenActualIgual("function")){//First(P) = eol, eof, var, if, for, id, return, write, prompt, function
			parser.add(1);
			P();
		}else 
			throw new RuntimeException("ERROR SINT�CTICO  (l�nea "+linea()+"): No se ha aceptado ning�n token en P'.");
	}

	public static void P(){//[2..5] 		P -> eol P | B eol P | F eol P | eof
		if(tokenActualIgual("eol")){//First(eolP) = eol
			parser.add(2);	
			compToken("eol");
			P();

		}else if (tokenActualIgual("var")
				|| tokenActualIgual("if") 
				|| tokenActualIgual("for") 
				|| tokenActualIgual("Id")
				|| tokenActualIgual("return")
				|| tokenActualIgual("write")
				|| tokenActualIgual("prompt")){//First(BeolP) = var, if, for, id, return, write, prompt
			parser.add(3);
			B();
			compToken("eol");
			P();
			//return new Entrada (0,null,null,0,null,null,0);

		}else if (tokenActualIgual("function")){//First(FeolP) = function
			parser.add(4);
			Entrada F = F();
			if (F.getTipo()!="ok") {
				System.err.println("ERROR SEM�NTICO  (l�nea "+linea()+"): El tipo de retorno de funci�n no coincide con el declarado.");
				errorAFichero("ERROR SEM�NTICO  (l�nea "+linea()+"): El tipo de retorno de funci�n no coincide con el declarado.");
			}
			compToken("eol");
			P();

		}else if (tokenActualIgual("eof")){//First(eof) = eof
			parser.add(5);
			compToken("eof");
			//escribir todas las tablas en un fichero
			//borrar todas las tablas

		}else {
			throw new RuntimeException("ERROR SINT�CTICO  (l�nea "+linea()+"): No se ha aceptado ning�n token en P.");

		}
	}

	public static void Z(){//[6..7]			Z -> eol Z | .
		if(tokenActualIgual("eol")){//First(eolZ) = eol
			parser.add(6);
			compToken("eol");
			Z();

		}else if(tokenActualIgual("var")
				|| tokenActualIgual("if") 
				|| tokenActualIgual("for") 
				|| tokenActualIgual("Id")
				|| tokenActualIgual("return")
				|| tokenActualIgual("write")
				|| tokenActualIgual("prompt")
				|| tokenActualIgual("{")
				|| tokenActualIgual("}")){//Follow(Z) = var, if, for, id, return, write, prompt, {, }
			parser.add(7);
			//Nothing

		}else
			throw new RuntimeException("ERROR SINT�CTICO  (l�nea "+linea()+"): No se ha aceptado ning�n token en Z.");
	}

	public static Entrada B(){//[8..11]		B -> var T id | if ( E ) S | S | for ( I ; E ; A ) eol Z { eol Z C }
		if(tokenActualIgual("var")){//First(varTid) = var
			parser.add(8);
			compToken("var");
			Entrada T = T();
			//Buscar si ya declarada -> Si: Error || No insertamos
			ArrayList <Entrada> TS = tablas.get(tablaActual);

			String nombre = tokenActual.token;
			compToken("Id");

			if ( buscaTSL(TS, nombre)!=null ) {
				System.err.println("ERROR SEM�NTICO  (l�nea "+linea()+"): Identificador "+ tokenActual.token + " ya declarado.");
				errorAFichero("ERROR SEM�NTICO  (l�nea "+linea()+"): Identificador "+ tokenActual.token + " ya declarado.");
				return new Entrada(0,"null","err",0,null,"null",0);
			}else{
				Entrada ent = new Entrada (addDesp(TS), nombre, T.getTipo(), T.getTam(), null, "null", 0);
				TS.add(ent); //se mete en la tabla la entrada de "id"
			}

			return new Entrada(0,"null","null",0,null,"null",0);

		}else if(tokenActualIgual("if")){// First(if(E)S) = if
			parser.add(9);
			creaTS();
			compToken("if");
			compToken("(");
			Entrada E = E();
			if (E.getTipo()!="boolean") {
				System.err.println("ERROR SEM�NTICO  (l�nea "+linea()+"): Condicional espera tipo l�gico.");
				errorAFichero("ERROR SEM�NTICO  (l�nea "+linea()+"): Condicional espera tipo l�gico.");
			}
			compToken(")");
			Entrada S = S();
			if (S.getTipo().equals("err") ){
				System.err.println("ERROR SEM�NTICO  (l�nea "+linea()+"): Sentencia del condicional incorrecta.");
				errorAFichero("ERROR SEM�NTICO  (l�nea "+linea()+"): Sentencia del condicional incorrecta.");
			}
			borraTS();
			return new Entrada(0,"null",S.getTipo(),0,null,S.getRet(),0);

		}else if(tokenActualIgual("Id")
				|| tokenActualIgual("return")
				|| tokenActualIgual("write")
				|| tokenActualIgual("prompt")){//First(S)= id, return, write, prompt
			parser.add(10);
			Entrada S = S();
			return new Entrada(0,"null",S.getTipo(),0,null,S.getRet(),0);

		}else if(tokenActualIgual("for")){//First(for(I;E;A)eolZ{eolZC}) = for
			parser.add(11);
			creaTS();
			compToken("for");
			compToken("(");
			Entrada I = I();

			if ( I.getTipo().equals("err") ){
				System.err.println("ERROR SEM�NTICO  (l�nea "+linea()+"): Sentencia incorrecta primera parte del for.");
				errorAFichero("ERROR SEM�NTICO  (l�nea "+linea()+"): Sentencia incorrecta primera parte del for.");
			}

			compToken(";");
			Entrada E = E();
			if ( !E.getTipo().equals("boolean") ){
				System.err.println("ERROR SEM�NTICO  (l�nea "+linea()+"): Sentencia incorrecta segunda parte del for.");
				errorAFichero("ERROR SEM�NTICO  (l�nea "+linea()+"): Sentencia incorrecta segunda parte del for.");
			}

			compToken(";");
			Entrada A = A();
			if (A.getTipo().equals("err") ){
				System.err.println("ERROR SEM�NTICO  (l�nea "+linea()+"): Sentencia incorrecta tercera parte del for.");
				errorAFichero("ERROR SEM�NTICO  (l�nea "+linea()+"): Sentencia incorrecta tercera parte del for.");
			}

			compToken(")");
			compToken("eol");
			Z();
			compToken("{");
			compToken("eol");
			Z();
			Entrada C = C();
			compToken("}");
			borraTS();
			return new Entrada(0,"null","null",0,null,C.getRet(),0);

		}else
			throw new RuntimeException("ERROR SINT�CTICO  (l�nea "+linea()+"): No se ha aceptado ning�n token en B.");
	}

	public static Entrada S(){//[12..15] 	S -> id S' | return X | write ( E ) | prompt ( id )
		if(tokenActualIgual("Id")){//First(idS') = id
			parser.add(12);
			String nombre = tokenActual.token;
			compToken("Id");
			Entrada Sp = Sp();
			ArrayList <Entrada> TS = tablas.get(tablaActual);
			Entrada ent = buscaTS(TS,nombre);

			if ( ent==null ) { // si no existe la creamos global y entera
				ent = new Entrada (addDesp(tablas.get(0)),nombre,"int",2,null,"null",0);
				tablas.get(0).add(ent); // int global
			}
			if ( !Sp.getTipo().equals("fun") && !ent.getTipo().equals(Sp.getTipo()) ) { // asignacion
				System.err.println("ERROR SEM�NTICO  (l�nea "+linea()+"): Se asigna tipo "+Sp.getTipo()+" a una variable tipo "+ent.getTipo()+".");
				errorAFichero("ERROR SEM�NTICO  (l�nea "+linea()+"): Se asigna tipo "+Sp.getTipo()+" a una variable tipo "+ent.getTipo()+".");
				return new Entrada (0,"null","err",0,null,"null",0); // no se asigna un int al global

			} else if ( Sp.getTipo().equals("fun") && !ent.getTipo().equals(Sp.getTipo()) ) {// minum () //Cambiado Sp.getRet por Sp.getTipo Sergio
				System.err.println("ERROR SEM�NTICO  (l�nea "+linea()+"): Se asigna a "+nombre+" de tipo "+ent.getTipo()+" un tipo "+Sp.getRet()+".");
				errorAFichero("ERROR SEM�NTICO  (l�nea "+linea()+"): Se asigna a "+nombre+" de tipo "+ent.getTipo()+" un tipo "+Sp.getRet()+".");
				return new Entrada (0,"null","err",0,null,"null",0); // incompatibilidad tipos

			} else if ( ent.getTipo().equals("fun") && !ent.getParam().equals(Sp.getParam()) ) { // Funcion ()
				System.err.println("ERROR SEM�NTICO  (l�nea "+linea()+"): Funci�n "+nombre+" espera argumentos ("+ent.divParam()+").");
				errorAFichero("ERROR SEM�NTICO  (l�nea "+linea()+"): Funci�n "+nombre+" espera argumentos ("+ent.divParam()+").");
				return new Entrada (0,"null","err",0,null,"null",0); 

			} 
			return new Entrada (0,"null","ok",0,null,"null",0); // llamada correcta a identificador


		}else if(tokenActualIgual("return")){// First(returnX) = return
			parser.add(13);
			compToken("return");
			Entrada X = X();
			return new Entrada (0,"null","ok",X.getTam(),null,X.getRet(),0);

		}else if(tokenActualIgual("write")){// First(write(E)) = write
			parser.add(14);
			compToken("write");
			compToken("(");
			Entrada E = E();
			compToken(")");
			return new Entrada (0,"null","ok",0,null,"null",0);

		}else if(tokenActualIgual("prompt")){// First(prompt(id)) = prompt
			parser.add(15);
			compToken("prompt");
			compToken("(");
			String id = tokenActual.token;
			compToken("Id");
			if ( buscaTS(tablas.get(tablaActual),id)==null ) 
				tablas.get(0).add(new Entrada ( addDesp(tablas.get(0)),id,"int",2,null,"null",0)  );

			compToken(")");
			return new Entrada (0,"null","ok",0,null,"null",0);

		}else
			throw new RuntimeException("ERROR SINT�CTICO  (l�nea "+linea()+"): No se ha aceptado ning�n token en S.");
	}

	public static Entrada Sp(){//[16..18]	S' -> = E | %= E | ( L ).
		if(tokenActualIgual("=")){//First(=E) = =
			parser.add(16);
			compToken("=");
			Entrada E = E();
			return new Entrada (0,"null",E.getTipo(),E.getTam(),null,"null",0);

		}else if(tokenActualIgual("%=")){// First(%=E) = %=
			parser.add(17);
			compToken("%=");
			Entrada E = E();
			if ( !E.getTipo().equals("int") ) {
				System.err.println("ERROR SEM�NTICO  (l�nea "+linea()+"): Se usa tipo "+E.getTipo()+" en %= , s�lo se permite int.");
				errorAFichero("ERROR SEM�NTICO  (l�nea "+linea()+"): Se usa tipo "+E.getTipo()+" en %= , s�lo se permite int.");
				return new Entrada (0,"null",E.getTipo(),0,null,"null",0);
			}				
			return new Entrada(0,"null","int",E.getTam(),null,"null",0);

		}else if(tokenActualIgual("(")){// First((L)) = (
			soloLlamada = true;
			parser.add(18);
			compToken("(");
			Entrada L = L();

			compToken(")");
			return new Entrada (0,"null","fun",0,L.getParam(),"null",0);

		}else
			throw new RuntimeException("ERROR SINT�CTICO  (l�nea "+linea()+"): No se ha aceptado ning�n token en S'.");
	}

	public static Entrada T() {//[19..21]	T -> int | boolean | char.
		if(tokenActualIgual("int")){//First(int) = int
			parser.add(19);
			compToken("int");
			return new Entrada (0,"null","int",2,null,"null",0);

		}else if(tokenActualIgual("boolean")){// First(boolean) = boolean
			parser.add(20);
			compToken("boolean");
			return new Entrada (0,"null","boolean",1,null,"null",0);

		}else if(tokenActualIgual("char")){// First(char) = char
			parser.add(21);
			compToken("char");
			return new Entrada (0,"null","char",1,null,"null",0);

		}else{
			throw new RuntimeException("ERROR SINT�CTICO  (l�nea "+linea()+"): No se ha aceptado ning�n token en T.");
		}

	}

	public static Entrada Tp(){//[22..23]	T' -> T | .
		if(tokenActualIgual("int") 
				|| tokenActualIgual("boolean")
				|| tokenActualIgual("char")){//First(T) = int, boolean, char
			parser.add(22);
			Entrada T = T();
			return new Entrada (0,"null","null",T.getTam(),null,T.getTipo(),0);

		}else if(tokenActualIgual("Id")){// Follow(T') = id
			parser.add(23);
			// Nothing
			return new Entrada (0,"null","null",0,null,"null",0);

		}else
			throw new RuntimeException("ERROR SINT�CTICO  (l�nea "+linea()+"): No se ha aceptado ning�n token en T'.");
	}

	public static Entrada X(){//[24..25]	X -> E | .
		if(tokenActualIgual("Id") 
				|| tokenActualIgual("(")
				|| tokenActualIgual("Num")
				|| tokenActualIgual("Cadena")
				|| tokenActualIgual("true")
				|| tokenActualIgual("false")){//First(E) = id, (, num, cadena, true, false
			parser.add(24);
			Entrada E = E();
			return new Entrada (0,"null","null",E.getTam(),null,E.getTipo(),0);

		}else if(tokenActualIgual("eol")){// Follow(X) = eol
			parser.add(25);
			// Nothing
			return new Entrada (0,"null","null",0,null,"null",0);

		}else
			throw new RuntimeException("ERROR SINT�CTICO  (l�nea "+linea()+"): No se ha aceptado ning�n token en X.");
	}

	public static Entrada L(){//[26..27]	L -> E L' | .
		if(tokenActualIgual("Id")
				||tokenActualIgual("(")
				|| tokenActualIgual("Num")
				|| tokenActualIgual("Cadena")
				|| tokenActualIgual("true")
				|| tokenActualIgual("false")){//First(ELp) = id, (, num, cadena, true, false
			parser.add(26);
			Entrada E = E();
			Entrada Lp = Lp();

			String args = E.getTipo() + Lp.getParam();
			return new Entrada (0,"null","null",0,args,"null",0);

		}else if(tokenActualIgual(")")){// Follow(L) = )
			parser.add(27);
			// Nothing
			return new Entrada (0,"null","null",0,"","null",0);

		}else
			throw new RuntimeException("ERROR SINT�CTICO  (l�nea "+linea()+"): No se ha aceptado ning�n token en L.");
	}

	public static Entrada Lp(){//[28..29]	L' -> , E L' | .
		if(tokenActualIgual(",")){//First(,EL') = ,
			parser.add(28);
			compToken(",");
			Entrada E = E();
			Entrada Lp = Lp();

			String args = E.getTipo() + Lp.getParam();
			return new Entrada (0,"null","null",0,args,"null",0);

		}else if(tokenActualIgual(")")){// Follow(Lp) = )
			parser.add(29);
			// Nothing
			return new Entrada (0,"null","null",0,"","null",0);

		}else
			throw new RuntimeException("ERROR SINT�CTICO  (l�nea "+linea()+"): No se ha aceptado ning�n token en L'.");
	}

	public static Entrada F(){//[30]		F -> function T' id ( G ) eol Z { eol Z C }
		if(tokenActualIgual("function")){//First(functionT'id(G)eolZ{eolZC}) = function
			parser.add(30);
			compToken("function");
			Entrada Tp = Tp();
			String nombre = tokenActual.token;//new
			compToken("Id");
			if ( buscaTS(tablas.get(tablaActual),nombre)!=null ) {
				System.err.println("ERROR SEM�NTICO  (l�nea "+linea()+"): Funci�n ya declarada.");
				errorAFichero("ERROR SEM�NTICO  (l�nea "+linea()+"): Funci�n ya declarada.");
				System.exit(0);
			}
			/*addTS id tipo ret Tp.tipo*/
			Integer padre = tablaActual;//new
			//String ret = Tp.getTipo();//new
			String ret = Tp.getRet();//sergio
			compToken("(");
			/*Crea sub-TS*/
			creaTS();
			Entrada G = G();  
			compToken(")");
			/*addTS id tipo funcion retorno tipo Tp, argumentos entrada params de G */
			String param = G.getParam();//new
			//Tabla simbolos padre:
			ArrayList <Entrada> TSP = tablas.get(padre);
			Entrada ent = new Entrada (TSP.get(TSP.size()-1).getDespl(), nombre, "fun", TSP.get(TSP.size()-1).getTam(), param, ret, 0);
			TSP.add(ent);
			compToken("eol");
			Z();
			compToken("{");
			compToken("eol");
			Z();
			Entrada C = C();
			compToken("}");
			/*Tipo devuelto por C sea el de Tp*/
			String tipoC = C.getRet();//new
			borraTS();//new
			return new Entrada (0,"null",(ret==tipoC)?"ok":"err",0,null,"null",0);

		}else
			throw new RuntimeException("ERROR SINT�CTICO  (l�nea "+linea()+"): No se ha aceptado ning�n token en F.");
	}

	public static Entrada C(){//[31..32]	C -> B eol Z C | .
		if(tokenActualIgual("var") 
				|| tokenActualIgual("if") 
				|| tokenActualIgual("for") 
				|| tokenActualIgual("Id") 
				|| tokenActualIgual("return") 
				|| tokenActualIgual("write")
				|| tokenActualIgual("prompt")){//First(BeolZC) = var, if, for, id, return, write, prompt
			parser.add(31);
			Entrada B = B();
			compToken("eol");
			Z();
			Entrada C = C();

			if ( !B.getRet().equals("null") && !C.getRet().equals("null") ){ // varios return
				if ( !B.getRet().equals(C.getRet()) ){ // return no coinciden
					System.err.println("ERROR SEM�NTICO  (l�nea "+linea()+"): Los return no son del mismo tipo.");
					errorAFichero("ERROR SEM�NTICO  (l�nea "+linea()+"): Los return no son del mismo tipo.");
					return new Entrada (0,"null","err",0,null,"null",0);
				}
				return new Entrada(0,"null","null",B.getTam(),null,B.getRet(),0);

			}else if ( !B.getRet().equals("null") && C.getRet().equals("null") ) {// B no null, C null
				return new Entrada(0,"null","null",B.getTam(),null,B.getRet(),0);

			}else if ( B.getRet().equals("null") && !C.getRet().equals("null") ) 	// B null, C no null
				return new Entrada(0,"null","null",C.getTam(),null,C.getRet(),0);

			return new Entrada (0,"null","null",0,null,"null",0); // ningun return de momento

		}else if(tokenActualIgual("}")){// Follow(C) = }
			parser.add(32);
			// Nothing
			return new Entrada (0,"null","null",0,null,"null",0);

		}else
			throw new RuntimeException("ERROR SINT�CTICO  (l�nea "+linea()+"): No se ha aceptado ning�n token en C.");
	}

	public static Entrada G(){//[33..34]	G -> T id G' | .
		if(tokenActualIgual("int") 
				|| tokenActualIgual("boolean")
				|| tokenActualIgual("char")){//First(TidG') = int, boolean, char
			parser.add(33);
			Entrada T = T();
			String id=null;
			if (tokenActualIgual("Id")) { id = tokenActual.token; }
			compToken("Id");
			ArrayList<Entrada> TSL = tablas.get(tablaActual);
			TSL.add(new Entrada (addDesp(TSL),id,T.getTipo(),T.getTam(),null,"null",0));
			Entrada Gp = Gp();

			String args = T.getTipo() + Gp.getParam();

			return new Entrada (0,"null","null",0,args,"null",0);

		}else if(tokenActualIgual(")")){// Follow(G) = )
			parser.add(34);
			// Nothing
			return new Entrada(0,"null","null",0,"","null",0);
		}else
			throw new RuntimeException("ERROR SINT�CTICO  (l�nea "+linea()+"): No se ha aceptado ning�n token en G.");
	}

	public static Entrada Gp(){//[35..36]	G' -> , T id G' | .
		if(tokenActualIgual(",")){//First(,TidG') = ,
			parser.add(35);
			compToken(",");
			Entrada T = T();
			String id=null;
			if (tokenActualIgual("Id")) { 
				id = tokenActual.token; 
			}
			compToken("Id");

			ArrayList<Entrada> TSL = tablas.get(tablaActual);
			TSL.add(new Entrada (addDesp(TSL),id,T.getTipo(),T.getTam(),null,"null",0));
			Entrada Gp = Gp();

			String args = T.getTipo() + Gp.getParam();

			return new Entrada (0,"null","null",0,args,"null",0);

		}else if(tokenActualIgual(")")){// Follow(K) = )
			parser.add(36);
			// Nothing
			return new Entrada(0,"null","null",0,"","null",0);
		}else
			throw new RuntimeException("ERROR SINT�CTICO  (l�nea "+linea()+"): No se ha aceptado ningun token en G'.");
	}

	public static Entrada I(){//[37..38]	I -> id = E | .
		if(tokenActualIgual("Id")){//First(id=E) = id
			parser.add(37);
			String id = tokenActual.token;
			Entrada ent = buscaTS(tablas.get(tablaActual), id);
			if (ent == null){ //no esta declarado el identificador{
				tablas.get(0).add(new Entrada (addDesp(tablas.get(0)), id, "int", 2, null, "null", 0));	
				ent = (Entrada)tablas.get(0).get(tablas.get(0).size()-1);
			}
			compToken("Id");
			compToken("=");
			Entrada E = E();
			if (!ent.getTipo().equals(E.getTipo())){
				System.err.println("ERROR SEM�NTICO  (l�nea "+linea()+"): Se asigna un tipo "+E.getTipo()+" a una variable tipo " + ent.getTipo()+".");
				errorAFichero("ERROR SEM�NTICO  (l�nea "+linea()+"): Se asigna un tipo "+E.getTipo()+" a una variable tipo " + ent.getTipo()+".");
				return new Entrada (0,"null","err",0,null,"null", 0);
			}			
			return new Entrada (0,id,E.getTipo(),0,null,"null",0);

		}else if(tokenActualIgual(";")){// Follow(I) = ;
			parser.add(38);
			// Nothing
			return new Entrada (0,"null","ok",0,null,"null",0);

		}else
			throw new RuntimeException("ERROR SINT�CTICO  (l�nea "+linea()+"): No se ha aceptado ning�n token en I.");
	}

	public static Entrada A(){//[39..40]	A -> id A' | .
		if(tokenActualIgual("Id")){//First(idA') = id
			parser.add(39);
			String id = tokenActual.token;
			Entrada ent = buscaTS(tablas.get(tablaActual), id);
			if (ent == null){//no esta declarado el identificador
				tablas.get(0).add(new Entrada (addDesp(tablas.get(0)), id, "int", 2, null, "null", 0));//int global
			}
			compToken("Id");
			Entrada Ap = Ap();
			if ( !ent.getTipo().equals(Ap.getTipo()) ) {
				System.err.println("ERROR SEM�NTICO  (l�nea "+linea()+"): Se asigna un tipo "+Ap.getTipo()+" a una variable tipo " + ent.getTipo()+".");
				errorAFichero("ERROR SEM�NTICO  (l�nea "+linea()+"): Se asigna un tipo "+Ap.getTipo()+" a una variable tipo " + ent.getTipo()+".");
				return new Entrada (0,"null","err",0,null,"null", 0);
			}
			return new Entrada (0,"null","ok",0,null,"null",0);

		}else if(tokenActualIgual(")")){// Follow(A) = )
			parser.add(40);
			// Nothing
			return new Entrada (0,"null","ok",0,null,"null",0);

		}else
			throw new RuntimeException("ERROR SINT�CTICO  (l�nea "+linea()+"): No se ha aceptado ning�n token en A.");
	}

	public static Entrada Ap(){//[41..42]	A' -> = E | %= E
		if(tokenActualIgual("=")){//First(=E) = =
			parser.add(41);
			compToken("=");
			Entrada E = E();
			return new Entrada (0,"null",E.getTipo(),0,null,"null",0);

		}else if(tokenActualIgual("%=")){// First(%=E) = %=
			parser.add(42);
			compToken("%=");
			Entrada E = E();
			if ( !E.getTipo().equals("int") ) {
				System.err.println("ERROR SEM�NTICO  (l�nea "+linea()+"): Se asigna tipo "+E.getTipo()+" en %= , s�lo se permite int.");
				errorAFichero("ERROR SEM�NTICO  (l�nea "+linea()+"): Se asigna tipo "+E.getTipo()+" en %= , s�lo se permite int.");
				return new Entrada (0,"null",E.getTipo(),0,null,"null", 0);
			}
			return new Entrada (0,"null","int",2,null,"null",0);

		}else
			throw new RuntimeException("ERROR SINT�CTICO  (l�nea "+linea()+"): No se ha aceptado ning�n token en A'.");
	}

	public static Entrada E(){//[43]		E -> R E'.
		if(tokenActualIgual("Id")
				||tokenActualIgual("(")
				|| tokenActualIgual("Num")
				|| tokenActualIgual("Cadena")
				|| tokenActualIgual("true")
				|| tokenActualIgual("false")){//First(RE') = id, (, num, cadena, true, false
			parser.add(43);
			Entrada R = R();
			Entrada Ep = Ep();
			if ( Ep.getTipo().equals("null" ))
				return new Entrada (0,"null",R.getTipo(),R.getTam(),null,"null",0);
			else if ( !R.getTipo().equals(Ep.getTipo()) && !Ep.getTipo().equals("err") && !R.getTipo().equals("err") ) {
				System.err.println("ERROR SEM�NTICO (l�nea "+linea()+"): Tipos incompatibles.");
				errorAFichero("ERROR SEM�NTICO (l�nea "+linea()+"): Tipos incompatibles.");
				return new Entrada (0,"null","err",0,null,"null",0);

			}else if ( !R.getTipo().equals(Ep.getTipo()) && (Ep.getTipo().equals("boolean") || R.getTipo().equals("boolean")) ) {
				System.err.println("ERROR SEM�NTICO  (l�nea "+linea()+"): Operaciones logicas s�lo con operandos l�gicos.");
				errorAFichero("ERROR SEM�NTICO  (l�nea "+linea()+"): Operaciones l�gicas s�lo con operandos l�gicos.");
				return new Entrada (0,"null","err",0,null,"null",0);

			} return new Entrada (0,"null","boolean",1,null,"null",0); //Operacion logica

		}else
			throw new RuntimeException("ERROR SINT�CTICO  (l�nea "+linea()+"): No se ha aceptado ning�n token en E.");
	}

	public static Entrada Ep(){//[44..45]	E' -> && R E' | .
		if(tokenActualIgual("&&")){//First(&&REp) = &&
			parser.add(44);
			compToken("&&");
			Entrada R = R();
			Entrada Ep = Ep();
			if ( (!Ep.getTipo().equals("null") && !Ep.getTipo().equals("boolean")) || !R.getTipo().equals("boolean") ){ // R nunca null, && solo entre booleans
				System.err.println("ERROR SEM�NTICO  (l�nea "+linea()+"): Operaciones l�gicas s�lo con operandos l�gicos.");
				errorAFichero("ERROR SEM�NTICO  (l�nea "+linea()+"): Operaciones l�gicas s�lo con operandos l�gicos.");
				return new Entrada (0,"null","err",0,null,"null",0);
			}
			return new Entrada (0,"null","boolean",1,null,"null",0);

		}else if(tokenActualIgual(")")
				|| tokenActualIgual(",")
				|| tokenActualIgual(";")
				|| tokenActualIgual("eol")){// Follow(Ep) = ), ,, ;, eol
			parser.add(45);
			// Nothing
			return new Entrada (0,"null","null",0,null,"null",0);

		}else
			throw new RuntimeException("ERROR SINT�CTICO  (l�nea "+linea()+"): No se ha aceptado ning�n token en E'.");
	}

	public static Entrada R(){//[46]		R -> U R'.
		if(tokenActualIgual("Id")||tokenActualIgual("(")
				|| tokenActualIgual("Num")
				|| tokenActualIgual("Cadena")
				|| tokenActualIgual("true")
				|| tokenActualIgual("false")){//First(UR') = id, (, num, cadena, true, false
			parser.add(46);
			Entrada U = U();
			Entrada Rp = Rp();
			if ( Rp.getTipo().equals("null"))
				return new Entrada (0,"null",U.getTipo(),U.getTam(),null,"null",0);

			else if ( !U.getTipo().equals(Rp.getTipo()) ) { //Rp!=null , U nunca es null
				System.err.println("ERROR SEM�NTICO  (l�nea "+linea()+"): S�lo se pueden comparar tipos iguales.");
				errorAFichero("ERROR SEM�NTICO  (l�nea "+linea()+"): S�lo se pueden comparar tipos iguales.");
				return new Entrada (0,"null","err",0,null,"null",0);
			}
			return new Entrada (0,"null","boolean",1,null,"null",0);

		}else
			throw new RuntimeException("ERROR SINT�CTICO  (l�nea "+linea()+"): No se ha aceptado ning�n token en R.");
	}

	public static Entrada Rp(){//[47..48]	R' -> == U | .
		if(tokenActualIgual("==")){//First(==U) = ==
			parser.add(47);
			compToken("==");
			Entrada U = U();
			return new Entrada (0,"null",U.getTipo(),U.getTam(),null,"null",0);

		}else if(tokenActualIgual("&&")
				||tokenActualIgual(")")
				||tokenActualIgual(",")
				|| tokenActualIgual(";")
				|| tokenActualIgual("eol")){// Follow(Rp) = &&, ), ,, ;, eol
			parser.add(48);
			// Nothing
			return new Entrada (0,"null","null",0,null,"null",0);

		}else
			throw new RuntimeException("ERROR SINT�CTICO  (l�nea "+linea()+"): No se ha aceptado ning�n token en R'.");
	}

	public static Entrada U(){//[49]		U -> V U'.
		if(tokenActualIgual("Id")||tokenActualIgual("(")
				|| tokenActualIgual("Num")
				|| tokenActualIgual("Cadena")
				|| tokenActualIgual("true")
				|| tokenActualIgual("false")){//First(VU') = id, (, num, cadena, true, false
			parser.add(49);
			Entrada V = V();
			Entrada Up = Up();
			if ( ! Up.getTipo().equals("null") && !V.getTipo().equals(Up.getTipo()) ) {
				System.err.println("ERROR SEM�NTICO  (l�nea "+linea()+"): S�lo se pueden sumar tipos iguales.");
				errorAFichero("ERROR SEM�NTICO  (l�nea "+linea()+"): S�lo se pueden sumar tipos iguales.");
				return new Entrada (0,"null","err",0,null,"null",0);
			}
			return new Entrada (0,"null",V.getTipo(),V.getTam(),null,"null",0);

		}else
			throw new RuntimeException("ERROR SINT�CTICO  (l�nea "+linea()+"): No se ha aceptado ning�n token en U.");
	}

	public static Entrada Up(){//[50..51]	U' -> + V U' | .
		if(tokenActualIgual("+")){//First(+VUp) = +
			parser.add(50);
			compToken("+");
			Entrada V = V();
			Entrada Up = Up();
			if ( !Up.getTipo().equals("null")  && !V.getTipo().equals(Up.getTipo()) ) { //V.tipo nunca es null
				System.err.println("ERROR SEM�NTICO  (l�nea "+linea()+"): S�lo se pueden sumar tipos iguales.");
				errorAFichero("ERROR SEM�NTICO  (l�nea "+linea()+"): S�lo se pueden sumar tipos iguales.");
				return new Entrada (0,"null","err",0,null,"null",0);
			}
			return new Entrada (0,"null",V.getTipo(),V.getTam(),null,"null",0);

		}else if(tokenActualIgual("==")
				||tokenActualIgual("&&")
				||tokenActualIgual(")")
				|| tokenActualIgual(",")
				|| tokenActualIgual(";")
				|| tokenActualIgual("eol")){// Follow(Up) = ==, &&, ), ,, ;, eol
			parser.add(51);
			// Nothing
			return new Entrada (0,"null","null",0,null,"null",0);

		}else
			throw new RuntimeException("ERROR SINT�CTICO  (l�nea "+linea()+"): No se ha aceptado ning�n token en U'.");
	}

	public static Entrada V(){//[52..57]	V -> id V' | ( E ) | entero | cadena | true | false.
		if(tokenActualIgual("Id")){//First(idV') = id
			parser.add(52);
			String id = tokenActual.token;
			compToken("Id");
			Entrada Vp = Vp();
			ArrayList<Entrada> TS = tablas.get(tablaActual);
			Entrada ent = buscaTS(TS,id);
			if ( ent!=null && ent.getTipo().equals("fun") ){ // Existe id funcion en TS
				if ( !ent.getParam().equals(Vp.getParam()) ) { // Params no coinciden
					System.err.println("ERROR SEM�NTICO  (l�nea "+linea()+"): Funci�n " + id +" espera argumentos ("+ent.divParam() +").");
					errorAFichero("ERROR SEM�NTICO  (l�nea "+linea()+"): Funci�n " + id +" espera argumentos ("+ent.divParam() +").");
					return new Entrada (0,id,ent.getRet()/*"err"*/,0,null,"null",0);
				} // id es una funcion y la llaman con parametros correctos
				return new Entrada (0,"null",ent.getRet(),ent.getTam(),null,"null",0);

			}else if (ent!=null && !ent.getTipo().equals("fun") ){ // Existe id y no es funcion
				if ( Vp.getParam()!=null ){ // Ha entrado por ()
					System.err.println("ERROR SEM�NTICO  (l�nea "+linea()+"): " + id +" no es funci�n.");
					errorAFichero("ERROR SEM�NTICO  (l�nea "+linea()+"): " + id +" no es funci�n.");
					return new Entrada (0,"null","err",0,null,"null",0);
				} // id es variable
				return new Entrada (0,"null",ent.getTipo(),ent.getTam(),null,"null",0);

			}else if ( ent==null ) { // id no declarado -> variable global entera   && Vp.getTipo()=="param" ) {
				tablas.get(0).add(new Entrada (addDesp(tablas.get(0)),id,"int",2,null,"null",0)); // anado id a TS Global
				if (Vp.getParam()!=null) {
					System.err.println("ERROR SEM�NTICO  (l�nea "+linea()+"): "+ id +" es int global, no lleva par�metros.");
					errorAFichero("ERROR SEM�NTICO  (l�nea "+linea()+"): "+ id +" es int global, no lleva par�metros.");
					return new Entrada(0,"null","err",0,null,"null",0);
				}
			} return new Entrada (0,"null","int",2,null,"null",0); // Global entera

		}else if(tokenActualIgual("(")){//First((E)) = (
			parser.add(53);
			compToken("(");
			Entrada E = E();
			compToken(")");
			return new Entrada (0,"null",E.getTipo(),E.getTam(),null,"null",0);

		}else if(tokenActualIgual("Num")){//First(entero) = entero
			parser.add(54);
			if ( Integer.parseInt(tokenActual.token) > 32767 ) {
				System.err.println("ERROR SEM�NTICO  (l�nea "+linea()+"): n�mero m�ximo permitido es 32767.");
				errorAFichero("ERROR SEM�NTICO  (l�nea "+linea()+"): n�mero m�ximo permitido es 32767.");
				return new Entrada(0,"null","err",0,null,"null",0);
			}
			compToken("Num");
			return new Entrada(0,"null","int",2,null,"null",0);

		}else if(tokenActualIgual("Cadena")){//First(cadena) = cadena
			parser.add(55);
			compToken("Cadena");
			return new Entrada(0,"null","char",1,null,"null",0);

		}else if(tokenActualIgual("true")){//First(true) = true
			parser.add(56);
			compToken("true");
			return new Entrada(0,"null","boolean",1,null,"null",0);

		}else if(tokenActualIgual("false")){//First(false) = false
			parser.add(57);
			compToken("false");
			return new Entrada(0,"null","boolean",1,null,"null",0);

		}else
			throw new RuntimeException("ERROR SINT�CTICO  (l�nea "+linea()+"): No se ha aceptado ning�n token en V.");
	}

	public static Entrada Vp(){//[58..59]	V' -> ( L ) | .
		if(tokenActualIgual("(")){//First((L)) = (
			parser.add(58);
			compToken("(");
			Entrada L = L();
			compToken(")");
			return new Entrada (0,"null","null",0,L.getParam(),"null",0);

		}else if(tokenActualIgual("+")
				||tokenActualIgual("==")
				|| tokenActualIgual("&&")
				||tokenActualIgual(")")
				|| tokenActualIgual(",")
				|| tokenActualIgual(";")
				|| tokenActualIgual("eol")){// Follow(Vp) = +, ==, &&, ), ,, ;, eol
			parser.add(59);
			// Nothing
			return new Entrada (0,"null","null",0,null,"null",0);

		}else
			throw new RuntimeException("ERROR SINT�CTICO  (l�nea "+linea()+"): No se ha aceptado ning�n token en V'.");
	}


	public static int linea(){
		if(tokenActual != null )
			return tokenActual.linea+1;
		return 0;
	}


	public static Entrada buscaTS (ArrayList <Entrada> TS, String nombre){

		Integer actual = tablaActual; 	// tabla que estamos analizando
		Integer p;						// Padre de la actual
		Entrada enTS = null;			// Aun no la ha encontrado

		while ( enTS==null && actual!=null  ) {

			if ( TS.size()!=1 ){			// Solo buscamos dentro de la tabla si contiene entradas
				int i = 1;
				Entrada ent = TS.get(i);
				while ( (i < TS.size()) && enTS==null ){ // mientras siga habiendo entradas y no encontrado
					if ( ent.getNom().equals(nombre) ){
						enTS = ent;	// encontrada
					}else{
						i++;
						if (i < TS.size())
							ent = TS.get(i);	// avanza puntero a siguiente entrada
					}
				}
			}
			p = ((Entrada)tablas.get(actual).get(0)).getPadre();
			if ( p==null ) { 
				actual=null;
			} // no lo ha encontrado ni en la global
			else { // puede seguir subiendo en la jerarquia
				TS = tablas.get(p);
				actual = p;
			}
		} return enTS;

	}

	public static Entrada buscaTSL (ArrayList <Entrada> TS, String nombre){
		if (TS.size()==1){ return null;}
		else{
			Entrada enTS = null;
			Entrada ent;
			for (int i=1; i<TS.size() && enTS == null; i++){
				ent = TS.get(i);
				if (ent.getNom().equals(nombre))
					enTS = ent;				
			}
			return enTS;
		}
	}

	public static void creaTS (){
		ArrayList <Entrada> TS = new ArrayList <Entrada>();
		TS.add(new Entrada (0, "null", "null", 0, null, "null", tablaActual));
		tablas.add(TS);
		tablaActual = tablas.size()-1;//la TS nueva generada al final de tablas
	}

	public static void borraTS(){
		tablaActual = ((Entrada) tablas.get(tablaActual).get(0)).getPadre();
	}

	public static int addDesp(ArrayList <Entrada> TS){
		//int estoy = TS.size()-1;
		//if(TS.get(estoy).getTipo().equals("fun")){
		//	estoy--;
		//}
		return TS.get(TS.size()-1).getDespl()+TS.get(TS.size()-1).getTam();

	}

}
class Entrada{

	private int			despl;  // Desplazamiento entrada   desplAnt+tamAnt
	private String 		nom;	// Nombre identificador
	private String 		tipo;	// Tipo identificador
	private int 		tam;	// Tamano entrada
	private String		param;
	private String 		ret;	// Tipo retorno
	private Integer 	padre;  // Tabla 

	public Entrada(int despl, String nom, String tipo, int tam,
			String param, String ret, Integer padre) {
		super();
		this.despl = despl;
		this.nom = nom;
		this.tipo = tipo;
		this.tam = tam;
		this.param = param;
		this.ret = ret;
		this.padre = padre;
	}

	public int getDespl() {
		return despl;
	}

	public String getNom() {
		return nom;
	}

	public String getTipo() {
		return tipo;
	}

	public int getTam() {
		return tam;
	}

	public String getParam() {
		return param;
	}

	public ArrayList<String> printParam() {
		ArrayList<String> params = new ArrayList<String>();
		for ( int i=0; i<param.length(); ) 
			if ( param.charAt(i)=='i' && param.charAt(i+1)=='n' && param.charAt(i+2)=='t' ) {
				params.add("int");
				i = i+3;
			} else if (param.charAt(i)=='c'&&param.charAt(i+1)=='h'&&param.charAt(i+2)=='a'&&param.charAt(i+3)=='r') {
				params.add("char");
				i = i+4;
			} else if ( param.charAt(i)=='b'&&param.charAt(i+1)=='o'&&param.charAt(i+2)=='o'&&param.charAt(i+3)=='l') {
				params.add("boolean");
				i = i+7;
			} 
		return params;
	}

	public String divParam() {
		String params = "";
		for ( int i=0; i<param.length(); ) 
			if ( param.charAt(i)=='i') {
				params = params + "int, ";
				i = i+3;
			} else if (param.charAt(i)=='c') {
				params = params + "char, ";
				i = i+4;
			} else if ( param.charAt(i)=='b') {
				params = params + "boolean, ";
				i = i+7;
			} 
		return params.substring(0,params.length()-2);
	}

	public String getRet() {
		return ret;
	}

	public Integer getPadre() {
		return padre;
	}

	@Override
	public String toString() {
		return  "Entrada [desplazamiento=" + despl + ", ident=" + nom
				+ ", tipo=" + tipo + ", tamano=" + tam + ", param=" + param + 
				", padre=" + padre + "]" ;
	}

}


