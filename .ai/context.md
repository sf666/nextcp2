# AI Context für nextCP/2                                                                                                                                                                                                                 
                                                                                                                                                                                                                                          
Diese Datei enthält wichtige Informationen für AI-Assistenten zur besseren Code-Generierung und -bearbeitung.                                                                                                                             
                                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
## Projektübersicht                                                                                                                                                                                                                       
                                                                                                                                                                                                                                          
nextCP/2 ist ein web-basierter UPnP Control Point mit:                                                                                                                                                                                    
                                                                                                                                                                                                                                          
- **Backend**: Java (Spring Boot, mindestens JDK 17)                                                                                                                                                                                      
                                                                                                                                                                                                                                          
- **Frontend**: Angular/TypeScript                                                                                                                                                                                                        
                                                                                                                                                                                                                                          
- **Build-Tools**: Maven (Backend), Yarn (Frontend)                                                                                                                                                                                       
                                                                                                                                                                                                                                          
- **UPnP-Stack**: JUPnP                                                                                                                                                                                                                   
                                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
## Projektstruktur                                                                                                                                                                                                                        
                                                                                                                                                                                                                                          

backend/                                                                                                                                                                                                                                  

├── nextcp2-assembly/          # Hauptanwendung (ausführbare JAR)                                                                                                                                                                         

├── nextcp2-codegen/          # Code-Generierung                                                                                                                                                                                          

├── nextcp2-external-information-provider/  # Last.fm Integration                                                                                                                                                                         

├── nextcp2-modelgen/         # DTO-Generierung                                                                                                                                                                                           

├── nextcp2-runtime/          # Spring Boot Runtime                                                                                                                                                                                       

└── nextcp2-device-driver/    # Gerätreiber (z.B. McIntosh)                                                                                                                                                                               

frontend/                                                                                                                                                                                                                                 

└── nextcp-ui/                # Angular-Anwendung                                                                                                                                                                                         

                                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
## Code-Generierung                                                                                                                                                                                                                       
                                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
### DTO-Generierung                                                                                                                                                                                                                       
                                                                                                                                                                                                                                          
- **Quelle**: `backend/nextcp2-codegen/src/main/resources/yaml/dto.yaml`                                                                                                                                                                  
                                                                                                                                                                                                                                          
- **Java-DTOs**: Generiert in `nextcp2-modelgen/src/main/java/nextcp/dto/`                                                                                                                                                                
                                                                                                                                                                                                                                          
- **TypeScript-DTOs**: Automatisch generiert in `frontend/nextcp-ui/src/app/service/dto.d.ts`                                                                                                                                             
                                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
**WICHTIG**: Generierte DTO-Dateien NIE manuell bearbeiten!                                                                                                                                                                               
                                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
### UPnP-Services                                                                                                                                                                                                                         
                                                                                                                                                                                                                                          
- Automatische Generierung von Java-Code für entdeckte UPnP-Services                                                                                                                                                                      
                                                                                                                                                                                                                                          
- Verwendet JUPnP-Stack                                                                                                                                                                                                                   
                                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
## Coding-Standards                                                                                                                                                                                                                       
                                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
### Java                                                                                                                                                                                                                                  
                                                                                                                                                                                                                                          
- **Mindestversion**: JDK 17                                                                                                                                                                                                              
                                                                                                                                                                                                                                          
- **Framework**: Spring Boot                                                                                                                                                                                                              
                                                                                                                                                                                                                                          
- **Annotationen**: Jackson für JSON (z.B. `@JsonProperty`, `@JsonInclude`)                                                                                                                                                               
                                                                                                                                                                                                                                          
- **Package-Struktur**: `nextcp.*`                                                                                                                                                                                                        
                                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
### TypeScript/Angular                                                                                                                                                                                                                    
                                                                                                                                                                                                                                          
- **Build-Tool**: Yarn                                                                                                                                                                                                                    
                                                                                                                                                                                                                                          
- **Konfiguration**: Angular CLI                                                                                                                                                                                                          
                                                                                                                                                                                                                       
You are an expert in TypeScript, Angular, and scalable web application development. You write functional, maintainable, performant, and accessible code following Angular and TypeScript best practices.

## TypeScript Best Practices

- Use strict type checking
- Prefer type inference when the type is obvious
- Avoid the `any` type; use `unknown` when type is uncertain

## Angular Best Practices

- Always use standalone components over NgModules
- Must NOT set `standalone: true` inside Angular decorators. It's the default in Angular v20+.
- Use signals for state management
- Implement lazy loading for feature routes
- Do NOT use the `@HostBinding` and `@HostListener` decorators. Put host bindings inside the `host` object of the `@Component` or `@Directive` decorator instead
- Use `NgOptimizedImage` for all static images.
  - `NgOptimizedImage` does not work for inline base64 images.

## Accessibility Requirements

- It MUST pass all AXE checks.
- It MUST follow all WCAG AA minimums, including focus management, color contrast, and ARIA attributes.

### Components

- Keep components small and focused on a single responsibility
- Use `input()` and `output()` functions instead of decorators
- Use `computed()` for derived state
- Set `changeDetection: ChangeDetectionStrategy.OnPush` in `@Component` decorator
- Prefer inline templates for small components
- Prefer Reactive forms instead of Template-driven ones
- Do NOT use `ngClass`, use `class` bindings instead
- Do NOT use `ngStyle`, use `style` bindings instead
- When using external templates/styles, use paths relative to the component TS file.

## State Management

- Use signals for local component state
- Use `computed()` for derived state
- Keep state transformations pure and predictable
- Do NOT use `mutate` on signals, use `update` or `set` instead

## Templates

- Keep templates simple and avoid complex logic
- Use native control flow (`@if`, `@for`, `@switch`) instead of `*ngIf`, `*ngFor`, `*ngSwitch`
- Use the async pipe to handle observables
- Do not assume globals like (`new Date()`) are available.
- Do not write arrow functions in templates (they are not supported).

## Services

- Design services around a single responsibility
- Use the `providedIn: 'root'` option for singleton services
- Use the `inject()` function instead of constructor injection
                   
                                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
## Build-Prozess                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
### Reihenfolge (wichtig!)                                                                                                                                                                                                                
                                                                                                                                                                                                                                          
1. **Erst Frontend**: `cd frontend/nextcp-ui && ./ng build`                                                                                                                                                                               
                                                                                                                                                                                                                                          
2. **Dann Backend**: `cd backend && mvn clean install package`                                                                                                                                                                            
                                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
### Ausgabeverzeichnisse                                                                                                                                                                                                                  
                                                                                                                                                                                                                                          
- **Frontend-Build**: `backend/nextcp2-runtime/src/main/resources/static`                                                                                                                                                                 
                                                                                                                                                                                                                                          
- **Hauptanwendung**: `backend/nextcp2-assembly/target/`                                                                                                                                                                                  
                                                                                                                                                                                                                                          
- **Gerätreiber**: `backend/nextcp2-device-driver/*/target/`                                                                                                                                                                              
                                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
## Konfiguration                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
### Standard-Konfigurationsdateipfade (Priorität):                                                                                                                                                                                        
                                                                                                                                                                                                                                          
1. System-Property 'configFile'                                                                                                                                                                                                           
                                                                                                                                                                                                                                          
2. `/etc/nextcp2/nextcp2Config.json`                                                                                                                                                                                                      
                                                                                                                                                                                                                                          
3. `USER_HOME/nextcp2Config.json`                                                                                                                                                                                                         
                                                                                                                                                                                                                                          
4. `WORK_DIR/nextcp2Config.json`                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
### Standard-Port                                                                                                                                                                                                                         
                                                                                                                                                                                                                                          
- **Backend**: 8085                                                                                                                                                                                                                       
                                                                                                                                                                                                                                          
- **Frontend-Dev**: Standard Angular-Dev-Server                                                                                                                                                                                           
                                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
## Wichtige Konventionen                                                                                                                                                                                                                  
                                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
### Dateiänderungen                                                                                                                                                                                                                       
                                                                                                                                                                                                                                          
- Generierte Dateien in `static/` NICHT in Repository committen                                                                                                                                                                           
                                                                                                                                                                                                                                          
- DTO-Dateien werden automatisch generiert - nie manuell bearbeiten                                                                                                                                                                       
                                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
### Dependencies                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
- Externe Abhängigkeiten über `build_dependencies.sh` installieren                                                                                                                                                                        
                                                                                                                                                                                                                                          
- Beispiel: musicbrainz Library                                                                                                                                                                                                           
                                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
## Device Driver                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
### McIntosh                                                                                                                                                                                                                              
                                                                                                                                                                                                                                          
- **Unterstützte Geräte**: MA9000, MA12000                                                                                                                                                                                                
                                                                                                                                                                                                                                          
- **Verbindung**: RS232/TCP-IP (z.B. USR-TCP232-302)                                                                                                                                                                                      
                                                                                                                                                                                                                                          
- **Features**: Power, Volume, Input Source Control                                                                                                                                                                                       
                                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
## Development Setup                                                                                                                                                                                                                      
                                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
### Backend Debug                                                                                                                                                                                                                         
                                                                                                                                                                                                                                          
- **Main Class**: `nextcp.NextcpApplicationStartup`                                                                                                                                                                                       
                                                                                                                                                                                                                                          
- **Location**: `backend/nextcp2-assembly/src/main/java/`                                                                                                                                                                                 
                                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
### Frontend Debug                                                                                                                                                                                                                        
                                                                                                                                                                                                                                          
- **Command**: `yarn start -c dev`                                                                                                                                                                                                        
                                                                                                                                                                                                                                          
- **Directory**: `frontend/nextcp-ui/`                                                                                                                                                                                                    
                                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
                                                                                                                                                                                                                                          
## Browser-Anforderungen                                                                                                                                                                                                                  
                                                                                                                                                                                                                                          
- Server-Sent-Events Support erforderlich                                                                                                                                                                                                 
                                                                                                                                                                                                                                          
- Chromium-basierte Browser empfohlen                                                                                                                                                                                                     

