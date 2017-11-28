using UnityEditor;
using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using UnityEditor.Callbacks;
#if UNITY_IPHONE
using UnityEditor.iOS.Xcode;
#endif

public class IOSBuildTools : Editor
{

    [PostProcessBuild]
    public static void OnPostProcessBuild(BuildTarget buildTarget, string path)
    {
#if UNITY_IPHONE

        string projPath = path + "/Unity-iPhone.xcodeproj/project.pbxproj";
        PBXProject pbxProj = new PBXProject();
        pbxProj.ReadFromFile(projPath);
        string target = pbxProj.TargetGuidByName(PBXProject.GetUnityTargetName());


        List<string> frameworks = new List<string>() {
			"SystemConfiguration.framework",
			"CoreTelephony.framework",
			"AudioToolbox.framework",
			"CoreAudio.framework",
			"AVFoundation.framework",
            "WebKit.framework",
            "CoreFoundation.framework",
            "AdSupport.framework",
            "CoreText.framework",
            "CFNetwork.framework",
            "CoreLocation.framework",
            "CoreMedia.framework",
            "CoreMotion.framework",
            "CoreVideo.framework",
            "Foundation.framework",
            "iAd.framework",
            "OpenAL.framework",
            "StoreKit.framework",
			"Security.framework"
        };

        foreach (var framework in frameworks)
        {
            pbxProj.AddFrameworkToProject(target, framework, false);
        }


        string XcodeLibPath = "usr/lib";
        List<string> libs = new List<string>() {
			"libstdc++.6.0.9.tbd",
            "libc++.tbd",
            "libz.tbd",
            "libsqlite3.0.tbd",
            "libz.1.2.5.tbd",
            "libresolv.9.tbd"
        };


        foreach (var lib in libs)
        {
            pbxProj.AddFileToBuild(target, pbxProj.AddFile(Path.Combine(XcodeLibPath, lib), "Frameworks/" + lib, PBXSourceTree.Sdk));
        }
			
        //string GVoice_Folder_Path = Path.Combine(new DirectoryInfo(Application.dataPath).Parent.FullName, "Plugins/iOS");
		//pbxProj.AddFileToBuild(target, pbxProj.AddFile(Path.Combine(GVoice_Folder_Path, "libGCloudVoice.a"), "Libraries/Plugins/iOS/libGCloudVoice.a", PBXSourceTree.Absolute));
        pbxProj.WriteToFile(projPath);

#endif
    }
}


