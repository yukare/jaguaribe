!define PRODUCT_NAME "Jaguaribe"
!define PRODUCT_VERSION "0.1"
!define INSTPATH "Jaguaribe 0.1"
!define PRODUCT_PUBLISHER "Jaguaribe.info"
!define PRODUCT_WEB_SITE "http://jaguaribe.info"
!define PRODUCT_UNINST_KEY "Software\Microsoft\Windows\CurrentVersion\Uninstall\${PRODUCT_NAME}"
!define PRODUCT_UNINST_ROOT_KEY "HKLM"
!define PRODUCT_STARTMENU_REGVAL "NSIS:StartMenuDir"

SetCompressor bzip2

; MUI 1.67 compatible ------
!include "MUI.nsh"

; File Association
; !include "FileAssociation.nsh"

; MUI Settings
!define MUI_ABORTWARNING
!define MUI_ICON "${NSISDIR}\Contrib\Graphics\Icons\modern-install.ico"
!define MUI_UNICON "${NSISDIR}\Contrib\Graphics\Icons\modern-uninstall.ico"

; Language Selection Dialog Settings
!define MUI_LANGDLL_REGISTRY_ROOT "${PRODUCT_UNINST_ROOT_KEY}"
!define MUI_LANGDLL_REGISTRY_KEY "${PRODUCT_UNINST_KEY}"
!define MUI_LANGDLL_REGISTRY_VALUENAME "NSIS:Language"

; Welcome page
!insertmacro MUI_PAGE_WELCOME
; License page
!insertmacro MUI_PAGE_LICENSE "..\src\text\LICENSE.TXT"
; Components
!insertmacro MUI_PAGE_COMPONENTS
; Directory page
!insertmacro MUI_PAGE_DIRECTORY

; Start menu page
var ICONS_GROUP
!define MUI_STARTMENUPAGE_NODISABLE
!define MUI_STARTMENUPAGE_DEFAULTFOLDER "Jaguaribe"
!define MUI_STARTMENUPAGE_REGISTRY_ROOT "${PRODUCT_UNINST_ROOT_KEY}"
!define MUI_STARTMENUPAGE_REGISTRY_KEY "${PRODUCT_UNINST_KEY}"
!define MUI_STARTMENUPAGE_REGISTRY_VALUENAME "${PRODUCT_STARTMENU_REGVAL}"
!insertmacro MUI_PAGE_STARTMENU Application $ICONS_GROUP
; Instfiles page
!insertmacro MUI_PAGE_INSTFILES
; Finish page
!define MUI_FINISHPAGE_SHOWREADME "$INSTDIR\${PRODUCT_NAME}_${PRODUCT_VERSION}\README.TXT"
!insertmacro MUI_PAGE_FINISH

; Uninstaller pages
!insertmacro MUI_UNPAGE_INSTFILES

; Language files
!insertmacro MUI_LANGUAGE "English"
!insertmacro MUI_LANGUAGE "PortugueseBR"

; Reserve files
;!insertmacro MUI_RESERVEFILE_INSTALLOPTIONS
ReserveFile '${NSISDIR}\Plugins\x86-ansi\InstallOptions.dll'

; MUI end ------

Name "${PRODUCT_NAME} ${PRODUCT_VERSION}"
OutFile "..\dist\setup.exe"
InstallDir "$PROGRAMFILES\Jaguaribe"
ShowInstDetails show
ShowUnInstDetails show
!include Sections.nsh
Function .onInit
  !insertmacro MUI_LANGDLL_DISPLAY
FunctionEnd

Section "Main" MAIN_SECTION
SectionIn RO
  SetOutPath "$INSTDIR\${PRODUCT_NAME}_${PRODUCT_VERSION}"
  SetOverwrite try
  File "..\jaguaribe.exe"
  File "..\src\text\LICENSE.TXT"
  File "..\src\text\README.TXT"
  SetOutPath "$INSTDIR\${PRODUCT_NAME}_${PRODUCT_VERSION}\dist"
  File "..\dist\Jaguaribe.jar"
  File /r "..\dist\lib"
   
  ; Shortcuts
  !insertmacro MUI_STARTMENU_WRITE_BEGIN Application
     
  !insertmacro MUI_STARTMENU_WRITE_END
SectionEnd

Section /o "Sources" SOURCES
  SetOutPath "$INSTDIR\${PRODUCT_NAME}_${PRODUCT_VERSION}"
  SetOverwrite try
  File /r "..\src" 
SectionEnd

Section /o "API documentation" APIDOCS
  SetOutPath "$INSTDIR\${PRODUCT_NAME}_${PRODUCT_VERSION}"
  SetOverwrite try
  File /r "..\dist\javadoc" 
SectionEnd
  
!macro CreateInternetShortcut FILENAME URL ICONFILE ICONINDEX
WriteINIStr "${FILENAME}.url" "InternetShortcut" "URL" "${URL}"
WriteINIStr "${FILENAME}.url" "InternetShortcut" "IconFile" "${ICONFILE}"
WriteINIStr "${FILENAME}.url" "InternetShortcut" "IconIndex" "${ICONINDEX}"
!macroend

Section -AdditionalIcons
  SetOutPath $INSTDIR
  !insertmacro MUI_STARTMENU_WRITE_BEGIN Application
  WriteIniStr "$INSTDIR\${PRODUCT_NAME}_${PRODUCT_VERSION}\${PRODUCT_NAME}.url" "InternetShortcut" "URL" "${PRODUCT_WEB_SITE}"
  CreateDirectory "$SMPROGRAMS\$ICONS_GROUP"
  CreateShortCut "$SMPROGRAMS\$ICONS_GROUP\Website.lnk" "$INSTDIR\${PRODUCT_NAME}_${PRODUCT_VERSION}\${PRODUCT_NAME}.url" 
  CreateShortCut "$SMPROGRAMS\$ICONS_GROUP\Uninstall.lnk" "$INSTDIR\${PRODUCT_NAME}_${PRODUCT_VERSION}\uninst.exe"
  CreateShortCut "$SMPROGRAMS\$ICONS_GROUP\Jaguaribe.lnk" "$INSTDIR\${PRODUCT_NAME}_${PRODUCT_VERSION}\jaguaribe.exe"
  !insertmacro MUI_STARTMENU_WRITE_END
SectionEnd

Section -Post
  WriteUninstaller "$INSTDIR\${PRODUCT_NAME}_${PRODUCT_VERSION}\uninst.exe"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DisplayName" "$(^Name)"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "UninstallString" "$INSTDIR\uninst.exe"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DisplayVersion" "${PRODUCT_VERSION}"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "URLInfoAbout" "${PRODUCT_WEB_SITE}"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "Publisher" "${PRODUCT_PUBLISHER}"
SectionEnd


Function un.onUninstSuccess
  HideWindow
  MessageBox MB_ICONINFORMATION|MB_OK "$(^Name) foi removido com sucesso do seu computador."
FunctionEnd

Function un.onInit
!insertmacro MUI_UNGETLANGUAGE
  MessageBox MB_ICONQUESTION|MB_YESNO|MB_DEFBUTTON2 "Tem certeza que quer remover completamente $(^Name) e todos os seus componentes?" IDYES +2
  Abort
FunctionEnd

Section Uninstall
  !insertmacro MUI_STARTMENU_GETFOLDER "Application" $ICONS_GROUP

  delete "$SMPROGRAMS\$ICONS_GROUP\Website.lnk"
  delete "$SMPROGRAMS\$ICONS_GROUP\Uninstall.lnk"
  delete "$SMPROGRAMS\$ICONS_GROUP\Jaguaribe.lnk"
  RMDir "$SMPROGRAMS\$ICONS_GROUP"
  RMDir /r "$INSTDIR\${PRODUCT_NAME}_${PRODUCT_VERSION}"
  RMDir "$INSTDIR"
  
  DeleteRegKey ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}"
  SetAutoClose true
SectionEnd