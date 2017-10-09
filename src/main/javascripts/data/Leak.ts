import Dataset from './Dataset';

export default interface Leak {
    href: URL;
    location: string;
    status: "FIXED" | "MISSING" | "UNREPAIRED";
    size: number | null;
    reportedOn: Date | null;
    fixedOn: Date | null;
    source: Dataset;
    vendorId: number | null;
}