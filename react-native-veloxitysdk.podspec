require 'json'

Pod::Spec.new do |s|

  s.name          = "react-native-veloxitysdk"
  s.version       = "3.5.6"
  s.summary       = "Veloxity SDK For ReactNative"
  s.authors       = { "veloxity" => "info@veloxity.net" }
  s.homepage      = "https://bitbucket.org/veloxity-inc/react-native-veloxitysdk#readme"
  s.license       = "Veloxity Properietary License"
  s.platforms     = { :ios => "10.0"}
  s.requires_arc  = true
  s.source        = { :git => "https://bitbucket.org/veloxity-inc/react-native-veloxitysdk.git", :tag => "v#{s.version}" }
  s.source_files  = "ios/**/*.{h,m}"
  
  s.dependency "React"
  s.dependency "React-Core"
  s.dependency "VeloxitySDK", "~> 3.9.3"
end
