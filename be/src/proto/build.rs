use std::path::Path;
use tonic_build::configure;

const PROTO_PATH: &str = "../../../proto";

fn main() -> Result<(), String> {
    let proto_dir = Path::new(PROTO_PATH);

    let dp = format!("{}/{}", PROTO_PATH, "datafusion.proto");
    let datafusion_path = Path::new(dp.as_str());

    let ep = format!("{}/{}", PROTO_PATH, "execute.proto");
    let execute_path = Path::new(ep.as_str());


    // proto definitions has to be there
    let descriptor_path = proto_dir.join("proto_descriptor.bin");

    configure()
        .build_server(true)
        .build_client(true)
        .file_descriptor_set_path(descriptor_path)
        .out_dir("src/generated")
        .compile(&[execute_path, datafusion_path], &[proto_dir])
        .map_err(|e| format!("protobuf compilation failed: {e}"))?;

    Ok(())
}


