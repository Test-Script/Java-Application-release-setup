{{- define "java-app.name" -}}
java-app
{{- end }}

{{- define "java-app.fullname" -}}
{{ .Release.Name }}-java-app
{{- end }}
