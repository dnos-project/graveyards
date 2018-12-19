load("//tools/src/build/bazel:generate_workspace.bzl", "COMPILE")

COMPILE_DEPS = COMPILE + [
    "@org_apache_httpcomponents//jar",
    "@com_googlecode_json_simple//jar",
    "@httpcomponents_httpcore//jar",
]

java_library(
    name = "dnos-tools-restapi-helper",
    srcs = glob(["*.java"]),
    visibility = ["//visibility:public"],
    deps = COMPILE_DEPS,
)
