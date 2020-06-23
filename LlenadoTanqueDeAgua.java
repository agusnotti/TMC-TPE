import java.io.BufferedReader;
import java.io.InputStreamReader;

public class LlenadoTanqueDeAgua {
	final static int SALIDA_CONSTANTE = 1;
	final static int SALIDA_K = 2;
	final static int SALIDA_W = 3;
	final static int SALIDA_G = 4;
	final static int TIEMPO_LIMITE = 1000000;
	final static double LIMITE_VACIADO = 0.000000001;

	public static void main(String[] args) {
		double tiempo; //t
		double alturaAgua; //h
		double entradaAgua;  //E
		double salidaAgua;  //S, K, G, W
		double areaTransversal;  //A
		double alturaTanque;  //C
		int tipoSalidaCaudal;
		
		
		BufferedReader entrada= new BufferedReader(new InputStreamReader(System.in));
		
		try {			
			
			//PARAMETROS INGRESADOS POR USUSARIO
			System.out.println("Ingrese los parametros para calcular el tiempo de llenado del tanque.");
			
			System.out.println("Ingrese cual es el area transversal del tanque (A)");
			areaTransversal=Double.valueOf(entrada.readLine());
			
			System.out.println("Ingrese cual es la altura del tanque (C)");
			alturaTanque=Double.valueOf(entrada.readLine());			
			
			System.out.println("Ingrese altura de agua inicial (h)");
			alturaAgua=Double.valueOf(entrada.readLine());
			
			System.out.println("Ingrese paso del tiempo (t)");
			tiempo=Double.valueOf(entrada.readLine());
			
			System.out.println("Ingrese la entrada de caudal constante (E)");		
			entradaAgua=Double.valueOf(entrada.readLine());
			
			
			System.out.println("Ingrese un valor entre 1 y 4 para seleccionar el tipo de salida de caudal");
			System.out.println("1. Salida constante de Agua");
			System.out.println("2. S(t) = K * t");
			System.out.println("3. S(t) = W * t²");
			System.out.println("4. S(t) =  G/A * h(t)");
			tipoSalidaCaudal = Integer.valueOf(entrada.readLine());
			
			System.out.println("Ingrese la salida de caudal");		
			salidaAgua=Double.valueOf(entrada.readLine());		
			
			
		
			calcularTiempoDeLlenado(alturaTanque, areaTransversal, alturaAgua, tiempo,  entradaAgua, tipoSalidaCaudal, salidaAgua);		
		
			
		} catch (Exception e) {
			System.out.println("error"+e);
		}

	}
	
	public static void calcularTiempoDeLlenado(double C, double A, double h, double t, double E, int tipoSalida, double salida) {
		double tiempo = 0;
		boolean llegoAMitadDeTanque = false;
		double salidaAgua = 0; 
		int iteraciones = 0;
		double tiempoMedio =0;
		int iteracionesMedio = 0;
		
		System.out.println("    i   |    t    |     E     |   S    |   h=h+(E-S)*t/A   |     V=A*t");
		
		while((h < C) && (tiempo < TIEMPO_LIMITE)) {
			tiempo= (iteraciones +1)*t; //incremento el tiempo
			iteraciones++;
			salidaAgua = obtenerSalidaDeAgua(salida, A, h, tipoSalida, tiempo);
			h = h + ((E - salidaAgua)*t/A);
				
			if((h >= C/2) && (llegoAMitadDeTanque == false)) {
				tiempoMedio = tiempo;
				iteracionesMedio=iteraciones;
				
				llegoAMitadDeTanque = true;
			}
			
			System.out.println("    "+iteraciones+"   |   "+tiempo+"   |    "+E+"    |  "+salidaAgua+"   |        "+h+"        |     "+(A*h));
			
		}
		System.out.println(" ");
		System.out.println("Llego a la mitad del tanque en "+tiempoMedio+ " segundos y " + iteracionesMedio + " iteraciones.");
		System.out.println("Tanque lleno en "+tiempo+" segundos y " + iteraciones + " iteraciones.");
		
		calcularTiempoDeVaciado(C, A, h, t,  0, tipoSalida, salida, iteraciones, tiempo);
	}
	
	public static void calcularTiempoDeVaciado(double C, double A, double h, double t, double E, int tipoSalida, double salida, int iteracionesLlenado, double tiempoLlenado) {
		double tiempo = 0;
		double salidaAgua = 0;
		int iteraciones = 0;
		double limite = 0;
		E = 0;
		
		//para el caso de que el tanque se llene
		if(h > C) {
			h = C;
		}
		
		if(tipoSalida == SALIDA_G) { //en la entrada G nunca llega a cero
			limite = LIMITE_VACIADO;
		}
		
		while((h > limite)) {
			tiempo= (iteraciones +1)*t;
			iteraciones++;
			salidaAgua = obtenerSalidaDeAgua(salida, A, h, tipoSalida, (tiempo+tiempoLlenado));
			h = h + ((E - salidaAgua)*t/A);
			//System.out.println("    "+(iteraciones+iteracionesLlenado)+"   |   "+(tiempo+tiempoLlenado)+"   |    "+E+"    |  "+salidaAgua+"   |        "+h+"        |     "+(A*h));

		}
		
		System.out.println("Tanque se vacia en "+ (tiempo+tiempoLlenado)+" segundos y "+ (iteraciones+iteracionesLlenado) + " iteraciones.");		
		System.out.println("total iteraciones: " + (iteracionesLlenado + iteraciones + 1));
	}
	
	public static double obtenerSalidaDeAgua(double salidaAgua, double A, double h, int tipoSalida, double tiempo) {
		double salida = 0;
		
		if(tipoSalida == SALIDA_CONSTANTE) {
			salida = salidaAgua;
		} else if(tipoSalida == SALIDA_K) {
			salida = salidaAgua*tiempo;
		}else if(tipoSalida == SALIDA_W) {
			salida = salidaAgua*(tiempo*tiempo);
		}else if(tipoSalida == SALIDA_G) {
			salida = salidaAgua/A*h;
		}
		
		return salida;
	}

}
