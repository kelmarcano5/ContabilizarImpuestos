package principal;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class Main {

	public static void main(String[] args) throws Throwable {
		// TODO Auto-generated method stub
		
		int result=0;
		ControlDB control = new ControlDB();
		control.cn.setAutoCommit(false);
		
		int smn_documento_id;
		int smn_entidades_id;
		int smn_auxiliar_sucursales_id;
		int smn_monedas_rf;
		int smn_documentos_generales_rf;
		int ccc_numero_documento;
		int smn_control_cierre_contable_id;
		int smn_clase_auxiliar_id;
		int smn_auxiliar_id;
		int smn_tasa;
		int smn_modulo_rf;
		int smn_tipo_documento_id;
		int smn_rel_modulo_documento_tipo_doc_id;
		long doc_numero_documento;
		int smn_clase_auxiliar_ord_rf;
		int smn_auxiliar_ord_rf;
		int doc_orden_compra_rf;
		int smn_centro_costo_rf;
		int smn_proyecto_rf;
		int smn_documentos_generales_rf_afecta;
		int doc_numero_doc_afecta;
		int doc_numero_control_doc_afect;
		int smn_codigos_impuestos_rf;
		int doc_numero_control_fiscal_inicial;
		int doc_numero_control_fiscal_ultimo;
		int doc_numero_control1_inicial;
		int doc_numero_control1_ultimo;
		int doc_numero_control2_inicial;
		int doc_numero_control2_ultimo;
		int doc_ano_comprobante;
		int doc_periodos_detalles_rf;
		int smn_tipo_comprobante_id;
		int doc_num_comprobante;
		int smn_elemento_rf;
		int smn_documento_id_cont;
		int smn_codigo_impuesto_rf;
		int smn_diccionario_id;
		int smn_tipo_elemento_id;
		int smn_tasa_rf;
		double total_monto_impuesto_ml = 0;
		double total_monto_impuesto_ma = 0;
		double total_monto_base_imponible = 0;
		double total_monto_base_excenta = 0;
		double total_monto_cabecera_monto_impuesto_ml = 0;
		double doc_tasa_cambio;
		double total_monto_ml;
		double total_monto_ma;
		double total_monto_detalle_base_imponible;
		double total_monto_detalle_base_excenta;
		double dod_monto_neto_ml;
		double dod_monto_neto_ma;
		double dod_monto_base_imponible;
		double dod_monto_base_excenta;
		//java.sql.Date mcc_fecha_registro=null;
		java.sql.Date imp_fecha_declaracion=null;
		//java.sql.Date doc_fecha_doc;
		java.sql.Date doc_fecha_rec;
		java.sql.Date doc_fecha_vcto;
		java.sql.Date doc_fecha_doc_afecta;
		java.sql.Date doc_fecha_comprobante;
		String ccc_estatus;
		String ccc_idioma;
		String ccc_usuario;
		String doc_estatus;
		String doc_planilla_importacion;
		String doc_numero_control;
		String doc_descripcion;
		String imp_descripcion;
		String dod_estatus;
		
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
		String fechaActual= sdformat.format(new Date());
		
		String sistemaOperativo = System.getProperty("os.name");
		String file;
		  
		if(sistemaOperativo.equals("Windows 7") || sistemaOperativo.equals("Windows 8") || sistemaOperativo.equals("Windows 10")) 
			file =  "C:/log/logContabilizarImpuestos_"+fechaActual+".txt";
		else
			file = "./logContabilizarImpuestos_"+fechaActual+".txt";
		
		File newLogFile = new File(file);
		FileWriter fw;
		String str="";
		
		if(!newLogFile.exists())
			fw = new FileWriter(newLogFile);
		else
			fw = new FileWriter(newLogFile,true);
		
		BufferedWriter bw=new BufferedWriter(fw);
		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
		Date parsed = date.parse(fechaActual);
        java.sql.Date sqlDate = new java.sql.Date(parsed.getTime());
        
        LocalDateTime locaDate = LocalDateTime.now();
        int hours  = locaDate.getHour();
        int minutes = locaDate.getMinute();
        int seconds = locaDate.getSecond();
        String hora = hours+":"+minutes+":"+seconds;
        
		try
		{
			str = "--------"+timestamp+"--------";	
			bw.write(str);
			bw.flush();
			bw.newLine();
			bw.newLine();
			
			str = "--- INICIO DEL PROCESO: 'Contabilizar Impuestos' ---";	
			bw.write(str);
			bw.flush();
			bw.newLine();
			
			str = "--- Consultando movimientos de impuestos para generar cierre contable ---";	
			bw.write(str);
			bw.flush();
			bw.newLine();
			
			ResultSet facturas=control.consultarImpuestos();
			
			if(getRecordCount(facturas)>0)
			{
				
				while(facturas.next())
				{	
					imp_fecha_declaracion=facturas.getDate("imp_fecha_declaracion");
					smn_entidades_id=facturas.getInt("smn_entidades_id");
					smn_auxiliar_sucursales_id=facturas.getInt("smn_auxiliar_sucursales_id");					
					smn_documento_id=facturas.getInt("smn_documento_id");
					smn_monedas_rf=facturas.getInt("smn_monedas_rf");
					smn_clase_auxiliar_id=facturas.getInt("smn_clase_auxiliar_id");
					smn_auxiliar_id=facturas.getInt("smn_auxiliar_id");
					total_monto_impuesto_ml=facturas.getDouble("total_monto_impuesto_ml");
					total_monto_impuesto_ma=facturas.getDouble("total_monto_impuesto_ma");
					total_monto_base_imponible=facturas.getDouble("total_monto_base_imponible");
					total_monto_base_excenta=facturas.getDouble("total_monto_base_excenta");
					smn_documentos_generales_rf=facturas.getInt("smn_documentos_generales_rf");
					smn_tipo_documento_id=facturas.getInt("smn_tipo_documento_id");
					
					ResultSet numero_cierre=control.consultarNumero_ccc();
					
					if(getRecordCount(numero_cierre)>0)
					{
						numero_cierre.next();
						if(numero_cierre.getString("ccc_numero_documento")!=null)
							ccc_numero_documento=numero_cierre.getInt("ccc_numero_documento")+1;
						else
							ccc_numero_documento=1;
					}
					else
					{
						ccc_numero_documento=1;
					}
					
					smn_tasa=0;
					ccc_estatus="CO";
					ccc_idioma="es";
					ccc_usuario="admin";
					
					ResultSet moduloImpuestos=control.moduloImpuestos();
					
					if(getRecordCount(moduloImpuestos)>0)
					{
						moduloImpuestos.next();
						smn_modulo_rf=moduloImpuestos.getInt("smn_modulos_id");
					}
					else
					{
						str = "--- No se encontro el modulo SMN_IMP=Impuestos ---";	
						bw.write(str);
						bw.flush();
						bw.newLine();
						result=1;
						break;
					}
					
					str = "--- Registrando cierre contable ---";	
					bw.write(str);
					bw.flush();
					bw.newLine();
					
					
					ResultSet RegistrarControlCierre=control.insertControl_cierre(smn_entidades_id, smn_auxiliar_sucursales_id, smn_documentos_generales_rf, smn_documento_id, imp_fecha_declaracion, ccc_numero_documento, total_monto_impuesto_ml, total_monto_impuesto_ma, smn_monedas_rf, smn_tasa, ccc_estatus, ccc_idioma, ccc_usuario, sqlDate, hora, smn_modulo_rf,smn_clase_auxiliar_id,smn_auxiliar_id);
					//ResultSet RegistrarControlCierre=control.insertControl_cierre(smn_entidades_id, smn_auxiliar_sucursales_id, smn_documentos_generales_rf, smn_documento_id, imp_fecha_declaracion, ccc_numero_documento, total_monto_impuesto_ml, total_monto_impuesto_ma, smn_monedas_rf, smn_tasa, ccc_estatus, ccc_idioma, ccc_usuario, sqlDate, hora, smn_modulo_rf,smn_clase_auxiliar_id,smn_auxiliar_id);
					RegistrarControlCierre.next();
					
					smn_control_cierre_contable_id=RegistrarControlCierre.getInt("smn_control_cierre_contable_id");
					
					str = "--- Cierre contable registrado ---";	
					bw.write(str);
					bw.flush();
					bw.newLine();
					
					str = "--- Actualizando movimientos de impuestos con el cierre contable registrado ---";	
					bw.write(str);
					bw.flush();
					bw.newLine();
					
					control.updateImpuestosCabecera(smn_entidades_id, smn_auxiliar_sucursales_id, smn_documento_id, smn_monedas_rf, imp_fecha_declaracion, smn_control_cierre_contable_id,smn_clase_auxiliar_id,smn_auxiliar_id, smn_documentos_generales_rf, smn_tipo_documento_id);
					
					str = "--- Movimientos de Impuestos actualizadas ---";	
					bw.write(str);
					bw.flush();
					bw.newLine();
					
					ResultSet tipoDocumento=control.tipoDocumento(smn_modulo_rf, smn_documentos_generales_rf, smn_tipo_documento_id);
					
					if(getRecordCount(tipoDocumento)>0)
					{
						tipoDocumento.next();
						smn_tipo_documento_id=tipoDocumento.getInt("smn_tipo_documento_id");
						smn_rel_modulo_documento_tipo_doc_id=tipoDocumento.getInt("smn_rel_modulo_documento_tipo_doc_id");
						
						str = "Datos para la tipoDocumento: " + "smn_tipo_documento_id: " + smn_tipo_documento_id + " smn_rel_modulo_documento_tipo_doc_id: " + smn_rel_modulo_documento_tipo_doc_id ;	
						bw.write(str);
						bw.flush();
						bw.newLine();
						
					}
					else
					{
						str = "--- No se encontro el tipo de documento contable ---";	
						bw.write(str);
						bw.flush();
						bw.newLine();
						result=1;
						break;
					}
					
					
					ResultSet numeroDoc=control.numeroDocumento();
					
					if(getRecordCount(numeroDoc)>0)
					{
						numeroDoc.next();
						if(numeroDoc.getString("doc_numero_documento")!=null)
							doc_numero_documento=numeroDoc.getLong("doc_numero_documento")+1;
						else
							doc_numero_documento=1;
					}
					else
					{
						doc_numero_documento=1;
					}
					
					smn_clase_auxiliar_ord_rf=0;
					smn_auxiliar_ord_rf=0;
					doc_orden_compra_rf=0;
					smn_centro_costo_rf=0;
					smn_proyecto_rf=0;
					//doc_fecha_doc=sqlDate;
					doc_fecha_rec=sqlDate;
					doc_fecha_vcto=null;
					doc_planilla_importacion=null;
					smn_documentos_generales_rf_afecta=0;
					doc_numero_doc_afecta=0;
					doc_numero_control_doc_afect=0;
					doc_fecha_doc_afecta=null;
					smn_codigos_impuestos_rf=0;
					doc_numero_control_fiscal_inicial=0;
					doc_numero_control_fiscal_ultimo=0;
					doc_numero_control1_inicial=0;
					doc_numero_control1_ultimo=0;
					doc_numero_control2_inicial=0;
					doc_numero_control2_ultimo=0;
					doc_estatus="R";
					doc_ano_comprobante=0;
					doc_periodos_detalles_rf=0;

					ResultSet tipoComprobante=control.tipoComprobante(smn_rel_modulo_documento_tipo_doc_id);
					
					if(getRecordCount(tipoComprobante)>0)
					{
						tipoComprobante.next();
						smn_tipo_comprobante_id=tipoComprobante.getInt("mcc_tipo_comp");
					}
					else
					{
						str = "--- No se encontro el tipo de comprobante contable ---";	
						bw.write(str);
						bw.flush();
						bw.newLine();
						result=1;
						break;
					}
					
					doc_num_comprobante=0;
					doc_fecha_comprobante=null;
					doc_numero_control=null;
					smn_elemento_rf=0;
					doc_descripcion=null;
					//smn_moneda_rf=0;
					doc_tasa_cambio=total_monto_impuesto_ml/total_monto_impuesto_ma;
					
					str = "--- Registrando documento contable ---";	
					bw.write(str);
					bw.flush();
					bw.newLine();
					
					
					total_monto_cabecera_monto_impuesto_ml = total_monto_impuesto_ml +
						    total_monto_base_imponible + total_monto_base_excenta;
						
							str = "total_monto_impuesto_ml: " + total_monto_impuesto_ml;
							bw.write(str);
							bw.flush();
							bw.newLine();
							
							str = "total_monto_base_imponible: " + total_monto_base_imponible;
							bw.write(str);
							bw.flush();
							bw.newLine();
							
							str = "total_monto_base_excenta: " + total_monto_base_excenta;
							bw.write(str);
							bw.flush();
							bw.newLine();
							
							str = "total_monto_cabecera_monto_impuesto_ml: " + total_monto_cabecera_monto_impuesto_ml;
							bw.write(str);
							bw.flush();
							bw.newLine();

					ResultSet insertDoc=control.insertDocumento(smn_modulo_rf, smn_entidades_id, smn_auxiliar_sucursales_id, 
							smn_documentos_generales_rf, smn_tipo_documento_id, doc_numero_documento, 
							smn_clase_auxiliar_id, smn_auxiliar_id, smn_clase_auxiliar_ord_rf, 
							smn_auxiliar_ord_rf, doc_orden_compra_rf, smn_centro_costo_rf, smn_proyecto_rf, 
							imp_fecha_declaracion, doc_fecha_rec, doc_fecha_vcto, doc_planilla_importacion, 
							total_monto_cabecera_monto_impuesto_ml, total_monto_impuesto_ma, doc_tasa_cambio, smn_documentos_generales_rf_afecta, 
							doc_numero_doc_afecta, doc_numero_control_doc_afect, doc_fecha_doc_afecta, 
							smn_codigos_impuestos_rf, doc_numero_control_fiscal_inicial, doc_numero_control_fiscal_ultimo, 
							doc_numero_control1_inicial, doc_numero_control1_ultimo, doc_numero_control2_inicial, 
							doc_numero_control2_ultimo, doc_estatus, doc_ano_comprobante, doc_periodos_detalles_rf, 
							smn_tipo_comprobante_id, doc_num_comprobante, "es", "admin", sqlDate, hora, doc_fecha_comprobante,
							doc_numero_control, smn_elemento_rf, doc_descripcion, 0);
					
					str = "--- Documento contable registrado ---";	
					bw.write(str);
					bw.flush();
					bw.newLine();
					
					insertDoc.next();
					smn_documento_id_cont=insertDoc.getInt("smn_documento_id_cont");
					
					str = "Datos para la consultarDetalles: " + "Entidad: " + smn_entidades_id + " Sucursal: " + smn_auxiliar_sucursales_id + " Documento_id; " +  smn_documento_id + " Moneda_rf: " +  smn_monedas_rf + " Fecha_registro: " + imp_fecha_declaracion + " auxiliar id: " + smn_auxiliar_id + " clase auxiliar id: " + smn_clase_auxiliar_id + " smn_documentos_generales_rf " + smn_documentos_generales_rf + " smn_tipo_documento_id " + smn_tipo_documento_id ;	
					bw.write(str);
					bw.flush();
					bw.newLine();
					
					ResultSet detalles=control.consultarDetalles(smn_entidades_id, smn_auxiliar_sucursales_id, smn_documento_id, smn_monedas_rf, imp_fecha_declaracion, smn_clase_auxiliar_id ,smn_auxiliar_id, smn_documentos_generales_rf, smn_tipo_documento_id);
					
				
					if(getRecordCount(detalles)>0)
					{

						while(detalles.next())
						{
							
							smn_codigo_impuesto_rf=detalles.getInt("smn_codigo_impuesto_rf");
							
							/* if(detalles.getString("centro_costo")!=null)
								smn_centro_costo_rf=detalles.getInt("centro_costo");
							else
							{
								str = "*No se encontro el centro de costo*";	
								bw.write(str);
								bw.flush();
								bw.newLine();
								result=1;
								break;
							} */
							
							if(detalles.getString("smn_clase_auxiliar_id")!=null)
								smn_clase_auxiliar_id=detalles.getInt("smn_clase_auxiliar_id");
							else
							{ 
								/*str = "*No se encontro la clase auxiliar*";	
								bw.write(str);
								bw.flush();
								bw.newLine();
								result=1;
								break;*/
								smn_clase_auxiliar_id=0;
							} 
							
							if(detalles.getString("smn_auxiliar_id")!=null)
								smn_auxiliar_id=detalles.getInt("smn_auxiliar_id");
							else
							{
								/*str = "*No se encontro el auxiliar*";	
								bw.write(str);
								bw.flush();
								bw.newLine();
								result=1;
								break;*/
								smn_auxiliar_id=0;
							} 
							
							total_monto_ml=detalles.getDouble("total_monto_detalle_impuesto_ml");
							
							if(detalles.getString("total_monto_detalle_impuesto_ma")!=null)
								total_monto_ma=detalles.getDouble("total_monto_detalle_impuesto_ma");
							else
								total_monto_ma=0;
							
							if(detalles.getString("total_monto_detalle_base_imponible")!=null)
								total_monto_detalle_base_imponible=detalles.getDouble("total_monto_detalle_base_imponible");
							else
								total_monto_detalle_base_imponible=0;
							
							if(detalles.getString("total_monto_detalle_base_excenta")!=null)
								total_monto_detalle_base_excenta=detalles.getDouble("total_monto_detalle_base_excenta");
							else
								total_monto_detalle_base_excenta=0;
							
							
							//validamos si el total_monto_ml tiene monto
							if(total_monto_ml != 0){
								
							ResultSet diccionario=control.consultarDiccionario();
							
							if(getRecordCount(diccionario)>0)
							{
								diccionario.next();
								smn_diccionario_id=diccionario.getInt("smn_diccionario_id");
							}
							else
							{
								str = "--- No se encontro el diccionario ---";	
								bw.write(str);
								bw.flush();
								bw.newLine();
								result=1;
								break;
							}
							
							//str = "Datos diccionario" + "smn_diccionario_id: " + smn_diccionario_id + " smn_modulo_rf: " + smn_modulo_rf ;
							//bw.write(str);
							//bw.flush();
							//bw.newLine();
							
							
							ResultSet tipoElemento=control.consultarTipoElemento(smn_diccionario_id, smn_modulo_rf);
							
							if(getRecordCount(tipoElemento)>0)
							{
								tipoElemento.next();
								smn_tipo_elemento_id=tipoElemento.getInt("smn_tipo_elemento_id");
							}
							else
							{
								str = "--- No se encontro el tipo de elemento ---";	
								bw.write(str);
								bw.flush();
								bw.newLine();
								result=1;
								break;
							}
							
							smn_tasa_rf=0;
							
							ResultSet componente=control.consultarComponente(smn_codigo_impuesto_rf);
							
							if(getRecordCount(componente)>0)
							{
								componente.next();
								imp_descripcion=componente.getString("imp_descripcion");
							}
							else
							{
								str = "--- 	 ---";	
								bw.write(str);
								bw.flush();
								bw.newLine();
								result=1;
								break;
							} 
							
							dod_monto_neto_ml=total_monto_ml;
							dod_monto_neto_ma=total_monto_ma;
							dod_monto_base_imponible=total_monto_detalle_base_imponible;
							dod_monto_base_excenta=total_monto_detalle_base_excenta;
							dod_estatus="RE";
							
							
							
							str = "--- Registrando documento detalle ---";	
							bw.write(str);
							bw.flush();
							bw.newLine();
							
							
							//total_monto_cabecera_monto_impuesto_ml = total_monto_impuesto_ml +
							//	    total_monto_base_imponible + total_monto_base_excenta;
							
							
								control.insertDocumentoDetalle(smn_documento_id_cont, smn_tipo_elemento_id, 
										smn_codigo_impuesto_rf, smn_clase_auxiliar_id, smn_auxiliar_id, 
										smn_proyecto_rf, smn_centro_costo_rf, total_monto_ml, smn_monedas_rf, 
										smn_tasa_rf, total_monto_ma, dod_estatus, "es", "admin", sqlDate, 
										hora, dod_monto_neto_ml, dod_monto_neto_ma
										, imp_descripcion);
							}
							
							
							//validamos si el total_monto_detalle_base_imponible tiene monto
							if (total_monto_detalle_base_imponible != 0){	
								
								ResultSet diccionario=control.consultarDiccionario1();
								
								if(getRecordCount(diccionario)>0)
								{
									diccionario.next();
									smn_diccionario_id=diccionario.getInt("smn_diccionario_id");
								}
								else
								{
									str = "--- No se encontro el diccionario ---";	
									bw.write(str);
									bw.flush();
									bw.newLine();
									result=1;
									break;
								}
								
								//str = "Datos diccionario" + "smn_diccionario_id: " + smn_diccionario_id + " smn_modulo_rf: " + smn_modulo_rf ;
								//bw.write(str);
								//bw.flush();
								//bw.newLine();
								
								
								ResultSet tipoElemento=control.consultarTipoElemento(smn_diccionario_id, smn_modulo_rf);
								
								if(getRecordCount(tipoElemento)>0)
								{
									tipoElemento.next();
									smn_tipo_elemento_id=tipoElemento.getInt("smn_tipo_elemento_id");
								}
								else
								{
									str = "--- No se encontro el tipo de elemento ---";	
									bw.write(str);
									bw.flush();
									bw.newLine();
									result=1;
									break;
								}
								
								smn_tasa_rf=0;
								
								ResultSet componente=control.consultarComponente(smn_codigo_impuesto_rf);
								
								if(getRecordCount(componente)>0)
								{
									componente.next();
									imp_descripcion=componente.getString("imp_descripcion");
								}
								else
								{
									str = "--- 	 ---";	
									bw.write(str);
									bw.flush();
									bw.newLine();
									result=1;
									break;
								} 
								
								dod_monto_neto_ml=total_monto_ml;
								dod_monto_neto_ma=total_monto_ma;
								dod_monto_base_imponible=total_monto_detalle_base_imponible;
								dod_monto_base_excenta=total_monto_detalle_base_excenta;
								dod_estatus="RE";
								
								
								
								str = "--- Registrando documento detalle ---";	
								bw.write(str);
								bw.flush();
								bw.newLine();
								
								
								//total_monto_cabecera_monto_impuesto_ml = total_monto_impuesto_ml +
								//	    total_monto_base_imponible + total_monto_base_excenta;
								
								
								control.insertDocumentoDetalle1(smn_documento_id_cont, smn_tipo_elemento_id, 
										smn_codigo_impuesto_rf, smn_clase_auxiliar_id, smn_auxiliar_id, 
										smn_proyecto_rf, smn_centro_costo_rf, dod_monto_base_imponible, smn_monedas_rf, 
										smn_tasa_rf, total_monto_ma, dod_estatus, "es", "admin", sqlDate, 
										hora, dod_monto_base_imponible, dod_monto_neto_ma
										, imp_descripcion);
								
							}
							
							
							//validamos si el total_monto_detalle_base_excenta tiene monto
							if (total_monto_detalle_base_excenta != 0){	
								
								ResultSet diccionario=control.consultarDiccionario2();
								
								if(getRecordCount(diccionario)>0)
								{
									diccionario.next();
									smn_diccionario_id=diccionario.getInt("smn_diccionario_id");
								}
								else
								{
									str = "--- No se encontro el diccionario ---";	
									bw.write(str);
									bw.flush();
									bw.newLine();
									result=1;
									break;
								}
								
								//str = "Datos diccionario" + "smn_diccionario_id: " + smn_diccionario_id + " smn_modulo_rf: " + smn_modulo_rf ;
								//bw.write(str);
								//bw.flush();
								//bw.newLine();
								
								
								ResultSet tipoElemento=control.consultarTipoElemento(smn_diccionario_id, smn_modulo_rf);
								
								if(getRecordCount(tipoElemento)>0)
								{
									tipoElemento.next();
									smn_tipo_elemento_id=tipoElemento.getInt("smn_tipo_elemento_id");
								}
								else
								{
									str = "--- No se encontro el tipo de elemento ---";	
									bw.write(str);
									bw.flush();
									bw.newLine();
									result=1;
									break;
								}
								
								smn_tasa_rf=0;
								
								ResultSet componente=control.consultarComponente(smn_codigo_impuesto_rf);
								
								if(getRecordCount(componente)>0)
								{
									componente.next();
									imp_descripcion=componente.getString("imp_descripcion");
								}
								else
								{
									str = "--- 	 ---";	
									bw.write(str);
									bw.flush();
									bw.newLine();
									result=1;
									break;
								} 
								
								dod_monto_neto_ml=total_monto_ml;
								dod_monto_neto_ma=total_monto_ma;
								dod_monto_base_imponible=total_monto_detalle_base_imponible;
								dod_monto_base_excenta=total_monto_detalle_base_excenta;
								dod_estatus="RE";
								
								
								
								str = "--- Registrando documento detalle ---";	
								bw.write(str);
								bw.flush();
								bw.newLine();
								
								
								//total_monto_cabecera_monto_impuesto_ml = total_monto_impuesto_ml +
								//	    total_monto_base_imponible + total_monto_base_excenta;
								
								
								
								control.insertDocumentoDetalle2(smn_documento_id_cont, smn_tipo_elemento_id, 
										smn_codigo_impuesto_rf, smn_clase_auxiliar_id, smn_auxiliar_id, 
										smn_proyecto_rf, smn_centro_costo_rf, dod_monto_base_excenta, smn_monedas_rf, 
										smn_tasa_rf, total_monto_ma, dod_estatus, "es", "admin", sqlDate, 
										hora, dod_monto_base_excenta, dod_monto_neto_ma
										, imp_descripcion);
							}
							
					/*		control.insertDocumentoDetalle(smn_documento_id_cont, smn_tipo_elemento_id, 
									smn_codigo_impuesto_rf, smn_clase_auxiliar_id, smn_auxiliar_id, 
									smn_proyecto_rf, smn_centro_costo_rf, total_monto_ml, smn_monedas_rf, 
									smn_tasa_rf, total_monto_ma, dod_estatus, "es", "admin", sqlDate, 
									hora, dod_monto_neto_ml, dod_monto_neto_ma
									, imp_descripcion); */
							
							str = "--- Documento detalle registrado ---";	
							bw.write(str);
							bw.flush();
							bw.newLine();
							
						} //end while detalles
					}
					else
					{
						str = "--- No se encontraron detalles para procesar ---";	
						bw.write(str);
						bw.flush();
						bw.newLine();
						result=1;
						break;
					}
					
					if(result==0)
					{
						str = "--- Actualizando estatus de los movimientos de impuestos ---";	
						bw.write(str);
						bw.flush();
						bw.newLine();
						
						control.updateImpuestos("CO", smn_entidades_id, smn_auxiliar_sucursales_id, smn_documento_id, smn_monedas_rf, imp_fecha_declaracion, smn_clase_auxiliar_id, smn_auxiliar_id, smn_documentos_generales_rf, smn_tipo_documento_id);
						
						str = "--- Estatus de los movimientos de impuestos actualizados";	
						bw.write(str);
						bw.flush();
						bw.newLine();
						
					}
					else
					{
						break;
					}
					
				}//end while facturas
			}
			else
			{
				str = "--- No se encontraron facturas para procesar ---";	
				bw.write(str);
				bw.flush();
				bw.newLine();
				result=1;
			}
		}
		catch(Throwable e)
		{
			control.cn.rollback();
			throw e;
		}
		finally
		{
			if(result == 0)
			{
				control.cn.commit();
				str = "--- Cambios efectuados en la base de datos ---";	
				bw.write(str);
				bw.flush();
				bw.newLine();
			}
			else
			{
				control.cn.rollback();
				str = "--- Cambios no efectuados en la base de datos ---";	
				bw.write(str);
				bw.flush();
				bw.newLine();
			}
			
			if(control.cn!=null)
				control.cn.close();
			
			str = "FINAL DEL PROCESO";	
			bw.write(str);
			bw.flush();
			bw.newLine();
			
			bw.close();
		}
	}
	
	public static int getRecordCount(ResultSet rs)
	{
		int total=0;
		
		try {
			boolean ultimo = rs.last();
			
			if (ultimo)
			{
		        total = rs.getRow();
		        rs.beforeFirst();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return total;
	}

}
