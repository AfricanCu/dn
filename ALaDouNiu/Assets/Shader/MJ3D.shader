// Toony Colors Pro+Mobile 2
// (c) 2014-2016 Jean Moreno



Shader "Toony Colors Pro 2/User/MJ3D"
{
	Properties
	{
		//TOONY COLORS
		_Color ("Color", Color) = (0.5,0.5,0.5,1.0)
		_HColor ("Highlight Color", Color) = (0.6,0.6,0.6,1.0)
		_SColor ("Shadow Color", Color) = (0.3,0.3,0.3,1.0)
		_HighlightMultiplier ("Highlight Multiplier", Range(0,4)) = 1
		_ShadowMultiplier ("Shadow Multiplier", Range(0,4)) = 1
		
		//DIFFUSE
		_MainTex ("Main Texture (RGB)", 2D) = "white" {}
		[NoScaleOffset]
		_Mask1 ("Mask 1 (Specular)", 2D) = "black" {}
		_DiffTint ("Diffuse Tint", Color) = (0.7,0.8,1,1)
		
		//TOONY COLORS RAMP
		_RampThreshold ("#RAMPF# Ramp Threshold", Range(0,1)) = 0.5
		_RampSmooth ("#RAMPF# Ramp Smoothing", Range(0.001,1)) = 0.1
		_RampThresholdOtherLights ("#RAMPF# Threshold (Other Lights)", Range(0,1)) = 0.5
		_RampSmoothOtherLights ("#RAMPF# Smoothing (Other Lights)", Range(0.001,1)) = 0.5
		//SPECULAR
		_SpecColor ("#SPEC# Specular Color", Color) = (0.5, 0.5, 0.5, 1)
		_Shininess ("#SPEC# Shininess", Range(0.0,2)) = 0.1
		_SpecSmooth ("#SPECT# Smoothness", Range(0,1)) = 0.05
		
		//Alpha Testing
		_Cutoff ("#CUTOUT# Alpha cutoff", Range(0,1)) = 0.5
	}
	
	SubShader
	{
		Tags {"Queue"="AlphaTest" "IgnoreProjector"="True" "RenderType"="TransparentCutout"}
		
		CGPROGRAM
		
		#pragma surface surf ToonyColorsCustom addshadow interpolateview
		#pragma target 2.5
		#pragma glsl
		
		#pragma multi_compile TCP2_SPEC_TOON
		
		//================================================================
		// VARIABLES
		
		fixed4 _Color;
		sampler2D _MainTex;
		sampler2D _Mask1;
		
		fixed _Shininess;
		fixed _Cutoff;
		
		struct Input
		{
			half2 uv_MainTex;
		};
		
		//================================================================
		// CUSTOM LIGHTING
		
		//Lighting-related variables
		fixed4 _HColor;
		fixed4 _SColor;
		fixed _HighlightMultiplier;
		fixed _ShadowMultiplier;
		float _RampThreshold;
		float _RampSmooth;
		float _RampThresholdOtherLights;
		float _RampSmoothOtherLights;
		fixed _SpecSmooth;
			fixed4 _DiffTint;
		
		//Custom SurfaceOutput
		struct SurfaceOutputCustom
		{
			fixed3 Albedo;
			fixed3 Normal;
			fixed3 Emission;
			half Specular;
			fixed Gloss;
			fixed Alpha;
		};
		
		inline half4 LightingToonyColorsCustom (inout SurfaceOutputCustom s, half3 lightDir, half3 viewDir, half atten)
		{
			s.Normal = normalize(s.Normal);
			fixed ndl = max(0, dot(s.Normal, lightDir));
			
	
	#if defined(UNITY_PASS_FORWARDBASE)
			fixed3 ramp = smoothstep(_RampThreshold-_RampSmooth*0.5, _RampThreshold+_RampSmooth*0.5, ndl);
	#else
			fixed3 ramp = smoothstep(_RampThresholdOtherLights-_RampSmoothOtherLights*0.5, _RampThresholdOtherLights+_RampSmoothOtherLights*0.5, ndl);
	#endif
			
			_SColor = lerp(_HColor, _SColor, _SColor.a * _ShadowMultiplier);	//Shadows intensity through alpha
			ramp = lerp(_SColor.rgb, _HColor.rgb * _HighlightMultiplier, ramp);
			fixed3 wrappedLight = saturate(_DiffTint.rgb + saturate(dot(s.Normal, lightDir)));
			ramp *= wrappedLight;
			
			//Specular
			half3 h = normalize(lightDir + viewDir);
			float ndh = max(0, dot (s.Normal, h));
			float spec = pow(ndh, s.Specular*128.0) * s.Gloss * 2.0;
			spec = smoothstep(0.5-_SpecSmooth*0.5, 0.5+_SpecSmooth*0.5, spec);
			spec *= atten;
			fixed4 c;
			c.rgb = s.Albedo * _LightColor0.rgb * ramp;
			c.rgb *= atten;
			c.rgb += _LightColor0.rgb * _SpecColor.rgb * spec;
			c.a = s.Alpha + _LightColor0.a * _SpecColor.a * spec;
			return c;
		}
		
		


		//================================================================
		// SURFACE FUNCTION
		
		void surf (Input IN, inout SurfaceOutputCustom o)
		{
			fixed4 mainTex = tex2D(_MainTex, IN.uv_MainTex);
			
			fixed4 mask1 = tex2D(_Mask1, IN.uv_MainTex);
			o.Albedo = mainTex.rgb * _Color.rgb;
			o.Alpha = mainTex.a * _Color.a;
			
			//Cutout (Alpha Testing)
			clip (o.Alpha - _Cutoff);
			
			//Specular
			o.Gloss = mask1.a;
			o.Specular = _Shininess;
		}
		
		ENDCG

	}
	
	Fallback "Diffuse"
	CustomEditor "TCP2_MaterialInspector"
}
