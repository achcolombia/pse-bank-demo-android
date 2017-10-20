# Aplicación de demostración para bancos. PSE móvil embebido.

## Introducción
PSE Móvil permite a los clientes que utilizan dispositivos móviles autorizar pagos desde una APP en vez de usar el navegador Web. Esto permite mayor control, recordar credenciales de forma segura y una experiencia nativa.

PSE Móvil puede ser embebido directamente en las aplicaciones de los bancos y con esto aumentar el valor de uso de la APP del banco.

Este repositorio es ejemplo y documentación del proceso para integrar la tecnología Browser2App (usada en PSE Móvil) en la APP del banco.

El proceso de autorización del pago consta de dos partes, la primera ocurre en el registro PSE, donde se pide al usuario que utilice un correo electrónico enrrolado o que enrrole uno y la segunda parte ocurre en el banco autorizador.

El banco que quiera implementar PSE embebido en su APP puede optar por que la biblioteca implemente el proceso completo de autorización (registro y banco) o sólamente registro y entregue el control a la APP para completar la autorización.

# Manual de uso la biblioteca nativa Browser2app en Android (com.browser2app:khenshin:*) 

Esta aplicación ha sido creada para demostrar la utilización de nuestra biblioteca khenshin.
Para poder ejecutar esta aplicación es necesario que tengas acceso a nuestro repositorio privado: https://dev.khipu.com/nexus/content/repositories/browser2app

Los pasos necesarios para utilizar la biblioteca nativa android para Browser2app son:

1. [Agregar los repositorios](#repositorios)
2. [Agregar las dependencias](#dependencias)
3. [Modificar la clase base de tu app](#clase-de-tu-aplicación)
4. [Configurar colores](#colores) y [vistas del proceso](#vistas)
5. [Invocar browser2app desde tu app](#invocación)
6. [Recibir la respuesta en tu app](#respuesta)


## Repositorios

Se debe incluir el [repositorio maven de khenshin](https://b2a.pse.com.co/nexus/content/repositories/browser2app) así como jcenter.


    allprojects {
		repositories {
			jcenter()
			maven {
				url 'https://b2a.pse.com.co/nexus/content/repositories/browser2app'
				credentials {
					username khenshinRepoUsername
					password khenshinRepoPassword
				}
			}
		}
	}
	
Los campos khenshinRepoUsername y khenshinRepoPassword te serán proporcionados por tu ejecutivo de ACH Colombia, se deben incluir en el archivo gradle.properties en la raiz del proyecto y sin incluir al sistema de control de versiones.

## Dependencias

Con los repositorios agregados puedes agregar el paquete khenshin a tu proyecto.

    compile 'com.browser2app:khenshin:2.6.6'
    
## Clase de tu aplicación

La clase principal de tu aplicación (la definida en el atributo android:name dentro del tag application en el AndroidManifest.xml) debe implementar la interfaz KhenshinApplication y en el constructor debe inicializar a Khenshin

	public class DemoBank extends Application implements KhenshinApplication {
	
		...
		
		private KhenshinInterface khenshin;
	
		@Override
		public KhenshinInterface getKhenshin() {
			return khenshin;
		}
	
		...
	
		public DemoBank() {
			super();
			khenshin = new Khenshin.KhenshinBuilder()
					.setApplication(this)
					.setAutomatonAPIUrl("https://b2a.pse.com.co/api/automata/")
					.setCerebroAPIUrl("https://b2a.pse.com.co/api/automata/")
					.setMainButtonStyle(Khenshin.CONTINUE_BUTTON_IN_FORM)
					.setAllowCredentialsSaving(true)
					.setHideWebAddressInformationInForm(false)
					.build();
		}
		
		...
	
	}
	
	

El parámetro MainButtonStyle puede tomar los valores Khenshin.CONTINUE_BUTTON_IN_FORM (El botón principal se pinta en bajo el formulario) o Khenshin.CONTINUE_BUTTON_IN_TOOLBAR (El botón principal se pinta en la barra de navegación).

Con AllowCredentialsSaving se enciende o apaga la opción de recordar credenciales en el dispositivo.

Con HideWebAddressInformationInForm se esconde la URL de navegación.


## Colores

En tu proyecto puedes determinar los colores que usará Khenshin en las pantallas de pago sobreescribiendo los siguiente parámetros en los recursos de tu proyecto (por ejemplo en un archivo colors.xml dentro de res/values)

    <color name="khenshin_primary">#ca0814</color> <!-- Color de la barra de navegación y botón principal-->
    <color name="khenshin_primary_dark">#580409</color> <!-- Color del status bar superior -->
    <color name="khenshin_primary_text">#ffffff</color> <!-- Color del texto en la barra de navegación -->
    <color name="khenshin_accent">#ca0814</color> <!-- Color de las decoraciones, por ejemplo barras de progreso -->
    
## Vistas

Para personalizar más aún la visualización de Khenshin puedes sobreescribir archivos de layout que se utilizan en el proceso de pago:


### khenshin_toolbar_title.xml

Este layout se usa en la barra de navegación en las páginas de salida (exito, fracaso o advertencia) y en las páginas del proceso si la barra del navegador está oculta.

	<?xml version="1.0" encoding="utf-8"?>
    <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
        style="@style/khenshin_toolbar_title">
        <TextView
            style="@style/khenshin_toolbar_title_text"
            android:text="@string/app_name"
        />
    </LinearLayout>

### khenshin_process_header.xml

Este layout se utiliza en todas las páginas intermedias del proceso de pago. La implementación recomendada es:

	<?xml version="1.0" encoding="utf-8"?>
	<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
				  style="@style/khenshin_vertical_wrapper">
		<ImageView
				style="@style/pament_header_image"
				android:id="@+id/merchantImage"/>
		<TextView
				android:id="@+id/merchantName"
				style="@style/khenshin_pay_title"
				android:layout_marginTop="10dp"
				android:layout_marginLeft="10dp"
				android:layout_marginRight="10dp"/>
	
		<LinearLayout
				android:layout_width="fill_parent"
				android:layout_height="wrap_content"
				android:layout_marginLeft="10dp"
				android:layout_marginRight="10dp"
				android:orientation="horizontal">
			<TextView
					style="@style/khenshin_dialog_title"
					android:layout_width="0dp"
					android:layout_weight=".8"
					android:layout_height="wrap_content"
					android:id="@+id/subject"/>
			<TextView
					style="@style/khenshin_dialog_title"
					android:layout_width="0dp"
					android:layout_weight=".2"
					android:gravity="right"
					android:layout_height="wrap_content"
					android:id="@+id/amount"/>
		</LinearLayout>
	
	</LinearLayout>

    
Khenshin reemplazará los valores de los siguientes campos (Tipo y android:id)

- ImageView: android:id="@+id/merchantImage"
- TextView: android:id="@+id/merchantName"
- TextView: android:id="@+id/subject"
- TextView: android:id="@+id/amount"
- TextView: android:id="@+id/paymentMethod"
    

### khenshin_process_success.xml

Se utiliza al finalizar el proceso de manera exitosa. La implementación por recomendada es:

	<?xml version="1.0" encoding="utf-8"?>
	<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
				style="@style/khenshin_page_body">
	
	
		<LinearLayout style="@style/khenshin_vertical_wrapper">
	
			<include layout="@layout/khenshin_finish_header"/>
			<LinearLayout style="@style/khenshin_vertical_wrapper_padded">
				<View style="@style/khenshin_horizontal_separator"/>
	
				<TextView
						android:id="@+id/title"
						style="@style/khenshin_dialog_title"/>
	
				<ImageView
						android:id="@+id/exitImage"
						android:layout_height="wrap_content"
						android:layout_width="wrap_content"
						android:layout_gravity="center_horizontal"
						android:src="@drawable/ic_transfer_ok"
						android:paddingBottom="10dp"/>
	
				<TextView
						android:id="@+id/message"
						style="@style/khenshin_dialog_message"
				/>
	
				<Button android:id="@+id/nextButton" android:visibility="gone" android:layout_width="match_parent" android:layout_height="wrap_content"
						android:text="@string/khenshinFinish" style="@style/khenshin_button"/>
			</LinearLayout>
	
		</LinearLayout>
	
	
	</ScrollView>
    
Khenshin reemplazará los valores de los siguientes campos (Tipo y android:id)

- TextView: android:id="@+id/title"
- TextView: android:id="@+id/message"

### khenshin_process_warning.xml

	<?xml version="1.0" encoding="utf-8"?>
	<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
				style="@style/khenshin_page_body">
	
	
		<LinearLayout style="@style/khenshin_vertical_wrapper">
	
			<include layout="@layout/khenshin_finish_header"/>
	
			<LinearLayout style="@style/khenshin_vertical_wrapper_padded">
	
				<View style="@style/khenshin_horizontal_separator"/>
	
				<TextView
						android:id="@+id/title"
						style="@style/khenshin_dialog_title"/>
	
	
				<ImageView
						android:id="@+id/exitImage"
						android:layout_height="wrap_content"
						android:layout_width="wrap_content"
						android:layout_gravity="center_horizontal"
						android:src="@drawable/ic_transfer_warning"
						android:paddingBottom="10dp"/>
	
				<TextView
						android:id="@+id/message"
						style="@style/khenshin_dialog_message"
				/>
	
				<Button android:id="@+id/nextButton" android:visibility="gone" android:layout_width="match_parent" android:layout_height="wrap_content"
						android:text="@string/khenshinFinish" style="@style/khenshin_button"/>
	
			</LinearLayout>
		</LinearLayout>
	
	
	</ScrollView>

Khenshin reemplazará los valores de los siguientes campos (Tipo y android:id)

- TextView: android:id="@+id/title"
- TextView: android:id="@+id/message"

### khenshin_process_failure.xml

	<?xml version="1.0" encoding="utf-8"?>
	<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
				style="@style/khenshin_page_body">
	
	
		<LinearLayout style="@style/khenshin_vertical_wrapper_padded">
	
			<TextView
				android:id="@+id/title"
				style="@style/khenshin_dialog_title"/>
	
	
			<ImageView
				android:id="@+id/exitImage"
				android:layout_height="wrap_content"
				android:layout_width="wrap_content"
				android:layout_gravity="center_horizontal"
				android:src="@drawable/ic_transfer_error"
				android:paddingBottom="10dp"/>
	
			<TextView
				android:id="@+id/message"
				style="@style/khenshin_dialog_message"
				/>
	
			<Button android:id="@+id/nextButton" android:visibility="gone" android:layout_width="match_parent" android:layout_height="wrap_content"
					android:text="@string/khenshinFinish" style="@style/khenshin_button"/>
	
	
		</LinearLayout>
	
	
	</ScrollView>
    
Khenshin reemplazará los valores de los siguientes campos (Tipo y android:id)

- TextView: android:id="@+id/title"
- TextView: android:id="@+id/message"    
    
## Invocación

La aplicación será invocada desde el sitio web del Registro PSE o desde una aplicación de un comercio. Para esto se usará un Intent con un "scheme" único para cada Banco. En el ejemplo el "scheme" es "demobankb2a".

La aplicación, por lo tanto, debe registrar un "intent-filter" asociada a la actividad que iniciará el proceso de autorización del pago, en en el caso de la Demo la actividad es "BrowsableActivity" y la declaración de la actividad junto con el filtro, en el AndroidManifest.xml es:
 
	 <activity
			 android:name=".BrowsableActivity"
			 android:theme="@style/khenshin.noab"
			 android:launchMode="singleTask">
		 <intent-filter>
			 <action android:name="android.intent.action.VIEW"/>
			 <category android:name="android.intent.category.DEFAULT"/>
			 <category android:name="android.intent.category.BROWSABLE"/>
			 <data android:scheme="demobankb2a"/>
		 </intent-filter>

	 </activity>

La actividad BrowsableActivity es responsable de recibir los parámetros de invocacion, iniciar el proceso de autorización y recibir la respuesta.

### Inicio de proceso de autorización


En el método onResume de la actividad podemos obtener los parámetros de invocación. En este caso el último segmento del campo "data" del "intent" que corresponde al ID de la instancia generada del autómata de pago asociado a la autorización pendiente.


	String automatonRequestId = getIntent().getData().getLastPathSegment();


Con ese parámetro se puede iniciar la actividad StartPaymentActivity con entregando el id de requerimento de autómata como un extra e iniciando la actividad para esperar la respuesta (startActivityForResult)


	Intent intent = new Intent(BrowsableActivity.this, StartPaymentActivity.class);
	intent.putExtra(KhenshinConstants.EXTRA_AUTOMATON_REQUEST_ID, automatonRequestId);
	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	setIntent(new Intent());
	startActivityForResult(intent, START_PAYMENT_REQUEST_CODE);



### Respuesta

En la actividad de tu aplicación que inició la actividad de pago se debe implementar el método onActivityResult

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == START_PAYMENT_REQUEST_CODE) {
			String exitUrl = data.getStringExtra(KhenshinConstants.EXTRA_INTENT_URL);
			if (resultCode == RESULT_OK) {
				Toast.makeText(MainActivity.this, "PAYMENT OK, exit url: " + exitUrl,
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(MainActivity.this, "PAYMENT FAILED, exit url: " + exitUrl,
						Toast.LENGTH_LONG).show();
			}
		}
	
	}
	
El parámetro requestCode debe ser el mismo que se envió al iniciar la actividad.

El parámetro resultCode será RESULT_OK si el pago terminó exitósamente o RESULT_CANCEL si el usuario no completó el pago.

En data vendrá un extra de nombre KhenshinConstants.EXTRA_INTENT_URL que tendrá la URL de salida . Si se está utilizando un autómata completo, esta será la URL del comercio a la que hay que enviar al usuario al final del pago, si se está usando un autómata que sólo automatiza el registro, esta será la URL a la que registro PSE enviaría al usuario para autorizar el pago en el Banco, de esa URL se deben extraer los datos necesarios para completar la autorización y posteriormente enviar al usuario de vuelta al comercio.

