type StepTitleProps = {
  title: string;
};

const StepTitle = ({ title }: StepTitleProps) => {
  return <h1 className="text-center font-bold pb-6">{title}</h1>;
};

export default StepTitle;
